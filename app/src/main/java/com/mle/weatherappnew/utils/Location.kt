package com.mle.weatherappnew.utils

sealed class Location {
    object Russia: Location()
    object World: Location()
}