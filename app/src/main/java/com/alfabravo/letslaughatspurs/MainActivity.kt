@file:OptIn(ExperimentalMaterial3Api::class)

package com.alfabravo.letslaughatspurs

import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alfabravo.letslaughatspurs.ui.theme.LetsLaughAtSpursTheme
import com.alfabravo.letslaughatspurs.ui.theme.PurpleGrey40
import com.alfabravo.letslaughatspurs.ui.theme.SpudsBlue
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LetsLaughAtSpursTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CenterAlignedTopAppBarExample()
                }
            }
        }
    }
}

@Composable
fun BasicCounter() {
    val context = LocalContext.current


    val d = Date()
    val currentDate: String = DateFormat.format("yyyy-MM-dd hh:mm:ss", d.time).toString()
//    Log.d("Date definition", currentDate)

    val leagueDiff = getLegacyDateDifference(
        "1961-04-26 17:00:00",
        currentDate
    )
    val trophyDiff = getLegacyDateDifference(
        "2008-02-24 17:00:00",
        currentDate
    )
    var sliderPosition by remember { mutableStateOf(0f) }
    val dateOptions: List<String> = listOf(
        stringResource(R.string.label_seconds),
        stringResource(R.string.label_hours),
        stringResource(R.string.label_days),
        stringResource(R.string.label_weeks),
        stringResource(R.string.label_years)
    )
    var leagueDateCalculationText by remember { mutableStateOf("") }
    var trophyDateCalculationText by remember { mutableStateOf("") }

    leagueDateCalculationText = defineDateCalculationLabel(context, sliderPosition, leagueDiff)
    trophyDateCalculationText = defineDateCalculationLabel(context, sliderPosition, trophyDiff)

    Column(
        modifier = Modifier
            .padding(30.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Card(
            modifier = Modifier
                .padding(5.dp)
        ) {
            Text(
                text = stringResource( R.string.slider_title, dateOptions[sliderPosition.toInt()]),
                modifier = Modifier.padding(10.dp)
            )
            Slider(
                value = sliderPosition,
                onValueChange = {
                    sliderPosition = it
                    leagueDateCalculationText = defineDateCalculationLabel(context, sliderPosition, leagueDiff)
                    trophyDateCalculationText = defineDateCalculationLabel(context, sliderPosition, trophyDiff)
                },
                colors = SliderDefaults.colors(
                    thumbColor = SpudsBlue,
                    activeTrackColor = PurpleGrey40,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                steps = 3,
                valueRange = 0f .. 4f,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
            )
        }
        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = stringResource(R.string.last_league_label),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.Start)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = leagueDateCalculationText,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .padding(start = 16.dp)
                .align(alignment = Alignment.Start),
            style = MaterialTheme.typography.bodyLarge.plus(
                TextStyle(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = SpudsBlue
                )
            )
        )
        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = stringResource(R.string.last_trophy_label),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.Start),
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = trophyDateCalculationText,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .padding(start = 16.dp)
                .align(alignment = Alignment.Start),
            style = MaterialTheme.typography.bodyLarge.plus(
                TextStyle(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = SpudsBlue
                )
            )
        )
        Spacer(modifier = Modifier.height(10.dp))

    }
}

@Composable
fun CenterAlignedTopAppBarExample() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        stringResource(R.string.main_activity_title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                /*navigationIcon = {
                    IconButton(onClick = { *//* do something *//* }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },*/
                actions = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding -> BasicCounter()
    }
}


/**
 * Calculate the difference between two dates, in different formats/time intervals
 * @return A map<String, Long> of calculated values for date diff, where the
 *  String keys are: days, hours, minutes, seconds, weeks, years
 */
private fun getLegacyDateDifference(
    fromDate: String, toDate: String, formatter: String= "yyyy-MM-dd HH:mm:ss" ,
    locale: Locale = Locale.getDefault()): Map<String, Long>
{
    val fmt = SimpleDateFormat(formatter, locale)
    val bgn = fmt.parse(fromDate)
    val end = fmt.parse(toDate)

    val milliseconds = end.time - bgn.time
    val days = milliseconds / 1000 / 3600 / 24
    val hours = milliseconds / 1000 / 3600
    val minutes = milliseconds / 1000 / 3600
    val seconds = milliseconds / 1000
    val weeks = days.div(7)
    val years = days.div(365)

    return mapOf(
        "days" to days, "hours" to hours,
        "minutes" to minutes, "seconds" to seconds,
        "weeks" to weeks, "years" to years
    )
}

private fun defineDateCalculationLabel(
    context: Context, position: Float = 0f, dateDiffMap: Map<String, Long>
): String {
    Log.d("DefiningLabel", "position: " + position)
    return when(position)  {
        1f -> context.resources.getString(
            R.string.interval_hours,
            NumberFormat.getNumberInstance().format(dateDiffMap["hours"]))
        2f -> context.resources.getString(
            R.string.interval_days,
            NumberFormat.getNumberInstance().format(dateDiffMap["days"]))
        3f -> context.resources.getString(
            R.string.interval_weeks,
            NumberFormat.getNumberInstance().format(dateDiffMap["weeks"]))
        4f -> context.resources.getString(
            R.string.interval_years,
            NumberFormat.getNumberInstance().format(dateDiffMap["years"])
        )
        else -> context.resources.getString(
            R.string.interval_seconds,
            NumberFormat.getNumberInstance().format(dateDiffMap["seconds"])
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainLaughPreview() {
    LetsLaughAtSpursTheme {
        BasicCounter()
    }
}