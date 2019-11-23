package actor

import org.scalatest.FlatSpec

class MyMathTest extends FlatSpec {

  behavior of "MyMathTest"

  it should "add" in {
    val myMath = MyMath()

    assert(myMath.add(5, 2) == 7)
    assert(myMath.add(-10, 10) == 0)
    assert(myMath.add(1690, 2000) == 3690)
  }

}
