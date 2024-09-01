package com.example.vodovoz.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.vodovoz.Data.Data
import com.example.vodovoz.Data.Products
import com.example.vodovoz.Data.TabItem
import com.example.vodovoz.R
import com.example.vodovoz.ViewModel.MainViewModel
import com.example.vodovoz.ui.theme.VodovozTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VodovozTheme {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(color = Color.White)
                        .padding(top = 8.dp)
                ) {
                    LoadProducts(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun LoadProducts(viewModel: MainViewModel){
    val products = viewModel.products.collectAsState()
    val productsLoading = viewModel.productsLoading.collectAsState()
    if (!productsLoading.value && products.value.tovary.isEmpty())
        viewModel.loadProducts()

    if (!productsLoading.value && products.value.tovary.isNotEmpty())
        InitTabs(products = products.value, viewModel)
}

@Composable
fun InitTabs(products: Products, viewModel: MainViewModel){
    val categories = mutableListOf<String>()
    products.tovary.forEach{ tovar ->
        categories.add(tovar.name)
    }

    val tabs = mutableListOf<TabItem>()
    categories.forEach {
        tabs.add(
            TabItem(
                title = it,
                selectedColor = colorResource(id = R.color.blue),
                unselectedColor = Color.Black
            )
        )
    }

    ShowTabs(tabs, viewModel)
    ShowProducts(products, viewModel)
}

@Composable
fun ShowTabs(tabs: MutableList<TabItem>, viewModel: MainViewModel){
    var selectedTabIndex by remember{
        mutableIntStateOf(0)
    }

    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier
            .background(Color.White),
        divider = {},
        indicator = {},
        edgePadding = 0.dp
    ) {
        tabs.forEachIndexed{ index, item ->
            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(12.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                )

            ){
                Tab(
                    modifier = Modifier
                        .background(color = Color.White),
                    selected = index == selectedTabIndex,
                    onClick = {
                        selectedTabIndex = index
                        viewModel.tabPosition.value = index
                    },
                    text = {
                        Text(
                            fontSize = 16.sp,
                            text = item.title,
                            color = if (index == selectedTabIndex) item.selectedColor else item.unselectedColor
                        )
                    },
                )
            }
        }
    }
}

@Composable
fun ShowProducts(products: Products, viewModel: MainViewModel){
    val tabPosition = viewModel.tabPosition.collectAsState()
    val tovary = products.tovary[tabPosition.value]
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LazyRow(
        modifier = Modifier
            .background(color = Color.White)
            .padding(8.dp),
        state = listState
    ){
      itemsIndexed(tovary.data){ index, item ->
          item.let {
              ProductView(data = it)
          }
      }
        coroutineScope.launch {
            listState.scrollToItem(0)
        }
    }
}

@Composable
fun ProductView(data: Data){
    var imageUrl: Any? = null
    if (data.detailPicture != "")
        imageUrl = "https://szorin.vodovoz.ru${data.detailPicture}"
    
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(
            top = 8.dp,
            bottom = 8.dp,
            start = 8.dp,
            end = 8.dp
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ){
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .background(Color.White)
        ) {
            SetImage(
                image = imageUrl,
                modifier = Modifier.size(150.dp),
                shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                contentScale = ContentScale.Fit
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 16.dp, start = 8.dp, bottom = 8.dp)
                    .background(Color.White)
                    .width(120.dp)
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        fontSize = 20.sp,
                        text = "${data.extendedPrice[0].price}₽",
                        color = Color.Black
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Card(
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .width(30.dp).height(30.dp)
                                .background(color = Color.Red)
                                .fillMaxSize()
                        ) {
                            Image(
                                modifier = Modifier
                                    .background(Color.Red),
                                painter = painterResource(id = R.drawable.baseline_shopping_basket_24),
                                contentDescription = null,
                                alignment = Alignment.Center,
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SetImage(
    image: Any?,
    modifier: Modifier,
    shape: RoundedCornerShape,
    contentScale: ContentScale,
){
    Card(
        shape = shape,
        modifier = Modifier.background(colorResource(id = R.color.dark_gray))
    ) {
        if (image == null)
            Image(
                painter = painterResource(id = R.drawable.glad),
                contentDescription = null,
                modifier = modifier,
                alignment = Alignment.Center,
                contentScale = ContentScale.Fit
            )
        else
            GlideImage(
                model = image,
                contentDescription = null,
                modifier = modifier,
                contentScale = contentScale
            )
    }
}