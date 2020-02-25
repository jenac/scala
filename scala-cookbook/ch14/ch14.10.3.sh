#!/bin/bash
exec scala "$0" "$@"
!#

object Hello extends App {
  println("hello, world")
}
