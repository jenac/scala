#!/bin/bash
exec scala "$0" "$@"
!#

import scala.io.StdIn._
Console.println("Hello")

//Console is imported by default
println("world")

val name = readLine("what is your name?")

println("how old are you?")
val age = readInt()

print(Console.RED)
println(s"$name is $age years old")
print(Console.RESET)