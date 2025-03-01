import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Composable
fun Modifier.christmasBackground(): Modifier {
  val p1x = animateBetween(0.41820982f, 0.22255483f, 0.33333334f, 0.15689304f)
  val p1y = animateBetween(0.22179085f, 0.42217737f, 0.33333334f, 0.17805377f)
  val p2x = animateBetween(0.8063271f, 0.52314824f, 0.6666667f, 0.8456789f)
  val p2y = animateBetween(0.41174963f, 0.19047627f, 0.33333334f, 0.16381979f)
  val p3x = animateBetween(0.22633749f, 0.5015431f, 0.33333334f, 0.1710392f)
  val p3y = animateBetween(0.6455969f, 0.7473042f, 0.6666667f, 0.8364378f)
  val p4x = animateBetween(0.62345684f, 0.7940568f, 0.6666667f, 0.8369341f)
  val p4y = animateBetween(0.7989127f, 0.49112603f, 0.6666667f, 0.8322974f)

  val colorPoints = listOf(
    listOf(
      Offset(0.0f, 0.0f) to Color(0xFF406538),
      Offset(0.33333334f, 0.0f) to Color(0xFF406538),
      Offset(0.6666667f, 0.0f) to Color(0xFF406538),
      Offset(1.0f, 0.0f) to Color(0xFF406538),
    ),
    listOf(
      Offset(0.0f, 0.33333334f) to Color(0xFF699A64),
      Offset(p1x, p1y) to Color(0xFF699A64),
      Offset(p2x, p2y) to Color(0xFF699A64),
      Offset(1.0f, 0.33333334f) to Color(0xFF699A64),
    ),
    listOf(
      Offset(0.0f, 0.6666667f) to Color(0xFF81B884),
      Offset(p3x, p3y) to Color(0xFF81B884),
      Offset(p4x, p4y) to Color(0xFF81B884),
      Offset(1.0f, 0.6666667f) to Color(0xFF81B884),
    ),
    listOf(
      Offset(0.0f, 1.0f) to Color(0xFF9BD89D),
      Offset(0.33333334f, 1.0f) to Color(0xFF9BD89D),
      Offset(0.6667f, 1.0f) to Color(0xFF9BD89D),
      Offset(1.0f, 1.0f) to Color(0xFF9BD89D),
    ),
  )

  return this.then(Modifier.meshGradient(points = colorPoints))
}

@Composable
private fun animateBetween(vararg values: Float): Float {
  val animatedPoint = remember { Animatable(values.first()) }

  LaunchedEffect(Unit) {
    while (true) {
      values.forEach { value ->
        animatedPoint.animateTo(
          targetValue = value,
          animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
        )
      }
    }
  }

  return animatedPoint.value
}
