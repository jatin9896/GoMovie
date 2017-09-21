package com.knoldus.example.services

trait ConfService {

  def readString(key: String): String

  def readListOfString(key: String): List[String]

}