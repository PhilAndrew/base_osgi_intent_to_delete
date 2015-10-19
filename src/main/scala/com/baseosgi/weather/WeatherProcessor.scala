package com.baseosgi.weather
/*
import java.text.SimpleDateFormat
import java.util.Date

import org.apache.camel.{Exchange, Processor}
import org.json.JSONObject
//remove if not needed

class WeatherProcessor extends Processor {

  override def process(exchange: Exchange) {
    println("in weather processor")
    val json = exchange.getIn.getBody(classOf[String])
    val obj = new JSONObject(json)
    val list = obj.getJSONArray("list")
    val day = list.getJSONObject(1)
    val dayTemp = day.getJSONObject("temp").getDouble("day")
    val timestampLong = day.getLong("dt")
    val date = new Date(timestampLong * 1000)
    val dateString = new SimpleDateFormat("yyyy-MM-dd").format(date)
    exchange.getOut.setBody(dateString + ": " + dayTemp + " Celsius", classOf[String])
  }
}
*/