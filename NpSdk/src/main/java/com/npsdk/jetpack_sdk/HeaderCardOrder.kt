package com.npsdk.jetpack_sdk

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.npsdk.R
import com.npsdk.jetpack_sdk.base.AppUtils.formatMoney
import com.npsdk.jetpack_sdk.repository.model.ValidatePaymentModel
import com.npsdk.jetpack_sdk.theme.fontAppBold
import com.npsdk.jetpack_sdk.theme.fontAppDefault
import kotlin.math.roundToInt


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HeaderOrder(data: ValidatePaymentModel) {

    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val nameMerchant: String = data.data.merchantInfo.name

    AnimatedContent(targetState = isExpanded) { showBoxCollapse ->
        Box(
            modifier = Modifier.fillMaxWidth().clip(shape = RoundedCornerShape(12.dp)).background(Color.White)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp)
            ) {

                Text(
                    text = "Thanh toán cho $nameMerchant",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontWeight = FontWeight.W400, color = colorResource(
                            id = R.color.titleText
                        ), fontSize = 12.sp, fontFamily = fontAppDefault
                    )
                )
                DataOrder.totalAmount?.let {
                    Text(
                        text = formatMoney(it),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontWeight = FontWeight.W600, fontSize = 18.sp, fontFamily = fontAppBold
                        )
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    Modifier.height(1.dp).fillMaxWidth().background(Color.Gray, shape = DottedShape(step = 5.dp))
                )
                if (showBoxCollapse) Spacer(modifier = Modifier.height(10.dp))


                if (showBoxCollapse) data.data.listPaymentData.map { rowItem ->
                    Row(
                        modifier = Modifier.padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            rowItem.name,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Start,
                            style = TextStyle(
                                color = colorResource(id = R.color.titleText),
                                fontWeight = FontWeight.W400,
                                fontSize = 12.sp,
                                fontFamily = fontAppDefault
                            )
                        )

                        Text(
                            text = if (rowItem.value is Double || rowItem.value is Int) formatMoney(rowItem.value) else rowItem.value.toString(),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End,
                            style = TextStyle(fontFamily = fontAppBold, fontSize = 12.sp)

                        )
                    }
                }

                // Tính phí giao dịch
                if (showBoxCollapse) DataOrder.totalAmount?.let {

                    // Phi = Tong cong tru di gia tri don hang
                    val fee = it - DataOrder.dataOrderSaved!!.data.amount

                    Row(
                        modifier = Modifier.padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            "Phí giao dịch",
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Start,
                            style = TextStyle(
                                color = colorResource(id = R.color.titleText),
                                fontWeight = FontWeight.W400,
                                fontSize = 12.sp,
                                fontFamily = fontAppDefault
                            )
                        )

                        Text(
                            text = formatMoney(fee),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End,
                            style = TextStyle(fontFamily = fontAppBold, fontSize = 12.sp)

                        )
                    }
                }

                if (showBoxCollapse) Box(
                    Modifier.height(1.dp).fillMaxWidth().background(Color.Gray, shape = DottedShape(step = 5.dp))
                )

                Row(
                    modifier = Modifier.fillMaxWidth().height(40.dp).padding(top = 8.dp, bottom = 6.dp).clickable {
                        isExpanded = !isExpanded
                    }, horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (showBoxCollapse) "Thu gọn" else "Xem thêm", textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontWeight = FontWeight.W600,
                            fontFamily = fontAppBold,
                            color = colorResource(R.color.blue),
                            fontSize = 12.sp
                        ),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Image(
                        painterResource(if (showBoxCollapse) R.drawable.arrow_up else R.drawable.arrow_down),
                        modifier = Modifier.size(10.dp),
                        contentDescription = null,
                    )
                }

            }
        }

    }
}

data class DottedShape(
    val step: Dp,
) : Shape {
    override fun createOutline(
        size: Size, layoutDirection: LayoutDirection, density: Density
    ) = Outline.Generic(Path().apply {
        val stepPx = with(density) { step.toPx() }
        val stepsCount = (size.width / stepPx).roundToInt()
        val actualStep = size.width / stepsCount
        val dotSize = Size(width = actualStep / 2, height = size.height)
        for (i in 0 until stepsCount) {
            addRect(
                Rect(
                    offset = Offset(x = i * actualStep, y = 0f), size = dotSize
                )
            )
        }
        close()
    })
}