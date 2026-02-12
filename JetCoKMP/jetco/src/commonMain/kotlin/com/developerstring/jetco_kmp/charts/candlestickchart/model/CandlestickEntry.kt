/*
 * Copyright 2026 by Developer Chunk.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.developerstring.jetco_kmp.charts.candlestickchart.model

import androidx.compose.runtime.Stable

/**
 * Represents the absolute price change of a candle.
 *
 * - [Bullish] → closing > opening  (price went up)
 * - [Bearish] → closing < opening  (price went down)
 * - [Neutral] → closing == opening (no change)
 */
enum class CandleChange {
    Bullish,
    Bearish,
    Neutral;

    internal companion object {
        fun forPrices(opening: Float, closing: Float): CandleChange =
            when {
                closing > opening -> Bullish
                closing < opening -> Bearish
                else -> Neutral
            }
    }
}

/**
 * Represents a single candle in a candlestick chart.
 *
 * Each entry contains the standard OHLC (Open, High, Low, Close)
 * data required to render a candlestick, plus an optional volume
 * value for volume-bar overlays.
 *
 * @property label X-axis label for this candle (e.g., date, time).
 * @property open  The opening price.
 * @property high  The highest price.
 * @property low   The lowest price.
 * @property close The closing price.
 * @property volume Optional trading volume for this period.
 * @property change The computed price direction ([CandleChange]).
 *
 * Example:
 * ```
 * val candle = CandlestickEntry(
 *     label  = "Jan 5",
 *     open   = 150f,
 *     high   = 160f,
 *     low    = 145f,
 *     close  = 155f,
 *     volume = 1_200_000f,
 * )
 * ```
 */
@Stable
data class CandlestickEntry(
    val label: String,
    val open: Float,
    val high: Float,
    val low: Float,
    val close: Float,
    val volume: Float = 0f,
    val change: CandleChange = CandleChange.forPrices(open, close),
) {
    init {
        require(low <= open && low <= close && low <= high) {
            "`low` ($low) can't be greater than `open`, `close`, or `high`."
        }
        require(high >= open && high >= close) {
            "`high` ($high) can't be less than `open` or `close`."
        }
    }
}
