package izumi.android.weatherreport

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import org.json.JSONException
import org.json.JSONObject

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.ArrayList
import java.util.HashMap

class WeatherReportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_report)

        val lvCityList = findViewById<ListView>(R.id.lvCityList)
        val cityList = ArrayList<Map<String, String>>()
        var city: MutableMap<String, String> = HashMap()

        city["name"] = "大阪"
        city["id"] = "270000"
        cityList.add(city)
        city = HashMap()
        city["name"] = "神戸"
        city["id"] = "280010"
        cityList.add(city)
        city = HashMap()
        city["name"] = "豊岡"
        city["id"] = "280020"
        cityList.add(city)
        city = HashMap()
        city["name"] = "京都"
        city["id"] = "260010"
        cityList.add(city)
        city = HashMap()
        city["name"] = "舞鶴"
        city["id"] = "260020"
        cityList.add(city)
        city = HashMap()
        city["name"] = "奈良"
        city["id"] = "290010"
        cityList.add(city)
        city = HashMap()
        city["name"] = "風屋"
        city["id"] = "290020"
        cityList.add(city)
        city = HashMap()
        city["name"] = "和歌山"
        city["id"] = "300010"
        cityList.add(city)
        city = HashMap()
        city["name"] = "潮岬"
        city["id"] = "300020"
        cityList.add(city)

        val from = arrayOf("name")
        val to = intArrayOf(android.R.id.text1)
        val adapter =
            SimpleAdapter(this@WeatherReportActivity, cityList, android.R.layout.simple_expandable_list_item_1, from, to)

        lvCityList.setAdapter(adapter)
        lvCityList.setOnItemClickListener(ListItemClickListener())
    }

    /**
     * リストが選択されたときの処理
     */
    private inner class ListItemClickListener : AdapterView.OnItemClickListener {

        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val item = parent.getItemAtPosition(position) as Map<String, String>
            val cityName = item["name"]
            val cityId = item["id"]
            val tvCityName = findViewById<TextView>(R.id.tvCityName)
            tvCityName.setText(cityName + "の天気: ")
            val tvWeatherTelop = findViewById<TextView>(R.id.tvWeatherTelop)
            val tvWeatherDesc = findViewById<TextView>(R.id.tvWeatherDesc)
            val receiver = WeatherInfoReceiver(tvWeatherTelop, tvWeatherDesc)
            receiver.execute(cityId)
        }
    }

    /**
     * 非同期でお天気データを取得するクラス。
     */
    private inner class WeatherInfoReceiver
    (
        private val _tvWeatherTelop: TextView,
        private val _tvWeatherDesc: TextView
    ): AsyncTask<String, String, String>() {

        public override fun doInBackground(vararg params: String): String {
            val id = params[0]
            val urlStr = "http://weather.livedoor.com/forecast/webservice/json/v1?city=$id"
            var result = ""
            var con: HttpURLConnection? = null
            var `is`: InputStream? = null
            try {
                val url = URL(urlStr)
                con = url.openConnection() as HttpURLConnection
                con.requestMethod = "GET"
                con.connect()
                `is` = con.inputStream
                result = is2String(`is`)
            } catch (ex: MalformedURLException) {
                println(ex.message)
            } catch (ex: IOException) {
                println(ex.message)
            } finally {
                con?.disconnect()
                if (`is` != null) {
                    try {
                        `is`.close()
                    } catch (ex: IOException) {
                    }

                }
            }

            return result
        }

        public override fun onPostExecute(result: String) {
            var telop = ""
            var desc = ""

            try {
                val rootJSON = JSONObject(result)
                val descriptionJSON = rootJSON.getJSONObject("description")
                desc = descriptionJSON.getString("text")
                val forecasts = rootJSON.getJSONArray("forecasts")
                val forecastNow = forecasts.getJSONObject(0)
                telop = forecastNow.getString("telop")
            } catch (ex: JSONException) {
            }

            _tvWeatherTelop.text = telop
            _tvWeatherDesc.text = desc
        }

        /**
         * InputStreamオブジェクトを文字列に変換するメソッド
         *
         * @param is 変換対象のInputStreamオブジェクト
         * @return 変換された文字列
         * @throws IOException
         */
        @Throws(IOException::class)
        private fun is2String(`is`: InputStream?): String {
            val reader = BufferedReader(InputStreamReader(`is`!!, "UTF-8"))
            return reader.readText()
        }
    }
}
