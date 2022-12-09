package ru.abyzbaev.weather_app.model.repository.contentprovider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import ru.abyzbaev.weather_app.App.Companion.getHistoryDao
import ru.abyzbaev.weather_app.model.room.*

private const val URI_ALL = 1 // URI для всех записей
private const val URI_ID = 2 // URI для конкретной записи
private const val ENTITY_PATH =
    "HistoryEntity" // Часть пути (будет отпределяться путь до HistoryEntity)

class HistoryWeatherContentProvider : ContentProvider() {
    private var authorities: String? = null //Адрес URI
    private lateinit var uriMatcher: UriMatcher // Помогает определить тип адреса URI

    //Типы данных
    private var entityContentType: String? = null // набор строк
    private var entityContentItemType: String? = null // одна строка

    private lateinit var contentUri: Uri // Адрес URI Provider

    override fun onCreate(): Boolean {
        authorities = "weatherAppProvider"
        //Вспомогательный класс для определения типа запроса
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        //Если нас интересуют все объекты
        uriMatcher.addURI(authorities, ENTITY_PATH, URI_ALL)
        //Если нас интересует только один объект
        uriMatcher.addURI(authorities, "$ENTITY_PATH/#", URI_ID)
        //Типы содержимого - все объекты
        entityContentType = "vnd.android.cursor.dir/vnd.$authorities.$ENTITY_PATH"
        //Типы содержимого - один объект
        entityContentType = "vnd.android.cursor.item/vnd.$authorities.$ENTITY_PATH"
        //Строка доступа к Provider
        contentUri = Uri.parse("content://$authorities/$ENTITY_PATH")
        return true
    }

    override fun getType(uri: Uri): String? {
        when (uriMatcher.match(uri)) {
            URI_ALL -> return entityContentType
            URI_ID -> return entityContentItemType
        }
        return null
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        //Получаем доступ к данным
        val historyDao: HistoryDao = getHistoryDao()
        //При помощи UriMatcher определяем запрашиваются все элементы или один
        val cursor = when (uriMatcher.match(uri)) {
            URI_ALL -> historyDao.getHistoryCursor() // запрос к БД для всех элементов
            URI_ID -> {
                //определяем id из URI адреса. Класс ContentUris помогает это сделать
                val id = ContentUris.parseId(uri)
                //Запрос к БД для одного элемента
                historyDao.getHistoryCursor(id)
            }
            else -> throw IllegalStateException("WRONG URI: $uri")
        }
        //Устанавливаем нотификацию на изменение данных в content_uri
        cursor.setNotificationUri(context!!.contentResolver, contentUri)
        return cursor
    }


    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        require(uriMatcher.match(uri) == URI_ID) { "Wrong URI: $uri" }
        //получаем доступ к данным
        val historyDao = getHistoryDao()
        //получаем id
        val id = ContentUris.parseId(uri)
        //удаляем запись по id
        historyDao.deleteById(id)
        //Нотификация на изменение Сursor
        context!!.contentResolver.notifyChange(uri, null)
        return 1
    }


    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        require(uriMatcher.match(uri) == URI_ALL) { "Wrong URI: $uri" }
        //получаем доступ к данным
        val historyDao = getHistoryDao()
        //добавляем запись о городе
        val entity = map(values)
        val id: Long = entity.id
        historyDao.insert(entity)
        val resultUri = ContentUris.withAppendedId(contentUri, id)
        //Уведомляем ContentResolver, что данные по адресу resultUri изменились
        context?.contentResolver?.notifyChange(resultUri, null)
        return resultUri
    }


    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        require(uriMatcher.match(uri) == URI_ALL) { "Wrong URI: $uri" }
        //получаем доступ к данным
        val historyDao = getHistoryDao()
        historyDao.update(map(values))
        context!!.contentResolver.notifyChange(uri, null)
        return 1
    }

    //Переводим ContentValues в HistoryEntity
    private fun map(values: ContentValues?): HistoryEntity {
        return if (values == null) {
            HistoryEntity(0, "", 0, 0)
        } else {
            val id = if (values.containsKey(ID)) values[ID] as Long else 0
            val city = values[CITY] as String
            val temp = values[TEMPERATURE] as Int
            val feelsLike = values[FEELS_LIKE] as Int
            HistoryEntity(id, city, temp, feelsLike)
        }
    }

}