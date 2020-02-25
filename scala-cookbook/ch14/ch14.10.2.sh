#!/bin/bash
exec scala "$0" "$@"
!#

class Person(val first: String, val last: String) {
  override def toString = first + "." + last
}

println(new Person("Akka", "Scala"))