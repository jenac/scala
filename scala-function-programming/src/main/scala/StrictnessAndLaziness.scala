object StrictnessAndLaziness {

  //5.1
  def toList[A](s: LazyList[A]): List[A] = s match { //Stream is obsolete
    case h #:: t => h :: toList(t)
    case _ => List()
  }

  //5.2
  def take[A](s: LazyList[A], n: Int): LazyList[A] = s match {
    case h #:: t if n > 0 => h #:: t.take(n - 1)
    case h #:: _ if n == 0 => h #:: LazyList.empty
    case _ => LazyList.empty
  }

  def drop[A](s: LazyList[A], n: Int): LazyList[A] = s match {
    case _ #:: t if n > 0 => t.drop(n - 1)
    case _ => s
  }

  //5.3
  def takeWhile[A](input: LazyList[A])(p: A => Boolean): LazyList[A] = input match {
    case h #:: t if p(h) => h #:: takeWhile(t)(p)
    case h #:: t => takeWhile(t)(p)
    case _ => LazyList.empty
  }

  //5.4
  def forAll[A](input: LazyList[A])(p: A => Boolean): Boolean = input match {
    case h #:: t if p(h) => forAll(t)(p)
    case _ #:: _ => false
    case _ => true
  }

  def forAll2[A](s: LazyList[A], f: A => Boolean): Boolean =
    s.foldRight(true)((a, b) => f(a) && b)

  //5.5
  def takeWhileViaFoldRight[A](input: LazyList[A])(p: A => Boolean): LazyList[A] =
    input.foldRight(LazyList.empty[A])((a, b) =>
      if (p(a)) a #:: b
      else LazyList.empty)

  //5.6
  def headOption[A](input: LazyList[A]): Option[A] =
    input.foldRight(None: Option[A])((h, _) => Some(h))

  //5.7
  def map[A, B](input: LazyList[A])(f: A => B): LazyList[B] =
    input.foldRight(LazyList.empty[B])((e, acc) => f(e) #:: acc)

  def filter[A](input: LazyList[A])(f: A => Boolean): LazyList[A] =
    input.foldRight(LazyList.empty[A])((e, acc) => if (f(e)) e #:: acc else acc)

  def append[A, B >: A](a: LazyList[A], b: LazyList[B]): LazyList[B] =
    a.foldRight(b)((e, acc) => e #:: acc)

  def flatMap[A, B](input: LazyList[A])(f: A => LazyList[B]): LazyList[B] =
    input.foldRight(LazyList.empty[B])((h, t) => append(f(h), t))

  //5.8
  def constant[A](a: A): LazyList[A] = a #:: constant(a)

  //5.9
  def from(n: Int): LazyList[Int] = n #:: from(n + 1)

  //5.10
  val fibs = {
    def go(f0: Int, f1: Int): LazyList[Int] = f0 #:: go(f1, f0 + f1)

    go(0, 1)
  }

  //5.11
  def unfold[A, S](z: S)(f: S => Option[(A, S)]): LazyList[A] = f(z) match {
    case Some((a, s)) => a #:: unfold(s)(f)
    case None => LazyList.empty[A]
  }

  //5.12
  def fibsViaUnfold =
    unfold((0, 1)) { case (f0, f1) => Some((f0, (f1, f0 + f1))) }

  def fromViaUnfold(n: Int) = unfold(n)(n => Some((n, n + 1)))

  def constantsViaUnfold(n: Int) = unfold(n)(n => Some(n, n))

  def onesViaUnfold = unfold(1)(_ => Some((1, 1)))

  //5.13
  def mapViaUnfold[A, B](i: LazyList[A])(f: A => B): LazyList[B] =
    unfold(i) {
      _ match {
        case (h #:: t) => Some((f(h), t))
        case _ => None
      }
    }

  def takeViaUnfold[A](input: LazyList[A], n: Int): LazyList[A] =
    unfold(input, n) {
      _ match {
        case (h #:: t, 1) => Some((h, (t, 0)))
        case (h #:: t, n) if n > 1 => Some((h, (t, n - 1)))
        case _ => None
      }
    }

  def takeWhileViaUnfold[A](input: LazyList[A])(f: A => Boolean): LazyList[A] =
    unfold(input) {
      _ match {
        case h #:: t if (f(h)) => Some(h, t)
        case _ => None
      }
    }

  def zipWith[A, B, C](s1: LazyList[A], s2: LazyList[B])(f: (A, B) => C): LazyList[C] =
    unfold((s1, s2)) {
      case (h1 #:: t1, h2 #:: t2) => Some(f(h1, h2), (t1, t2))
      case _ => None
    }

  def zipAll[A, B](s1: LazyList[A], s2: LazyList[B]): LazyList[(Option[A], Option[B])] = zipWithAll(s1, s2)((_, _))

  def zipWithAll[A, B, C](s1: LazyList[A], s2: LazyList[B])(f: (Option[A], Option[B]) => C): LazyList[C] =
    unfold((s1, s2)) {
      case (LazyList(), LazyList()) => None
      case (h #:: t, LazyList()) => Some(f(Some(h), None) -> (t, LazyList()))
      case (LazyList(), h #:: t) => Some(f(None, Some(h)) -> (LazyList() -> t))
      case (h1 #:: t1, h2 #:: t2) => Some(f(Some(h1), Some(h2)) -> (t1 -> t2))
    }

  //5.14
  def startsWith[A](whole: LazyList[A], part: LazyList[A]): Boolean =
    forAll(zipAll(whole, part).takeWhile(!_._2.isEmpty)) {
      case (h, h2) => h == h2
    }
}
