package example.otherscope

class Foo {
  def exec(f:(String) => String, name: String): String = {
    f(name)
  }
}