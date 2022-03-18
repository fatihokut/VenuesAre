package com.adyen.android.assignment.ui.model

class SampleVenues {

    fun initVenueList(): ArrayList<Venue> {
        return arrayListOf(
            Venue(
                name = "Starbucks",
                categoryId = 1,
                distance = 5
            ),
            Venue(
                name = "McDonalds",
                categoryId = 3,
                distance = 7
            ),
            Venue(
                name = "Pizza Hut",
                categoryId = 3,
                distance = 6
            ),
            Venue(
                name = "Zara",
                categoryId = 9,
                distance = 11
            ),
            Venue(
                name = "GameStop",
                categoryId = 12,
                distance = 28
            ),
            Venue(
                name = "Irish Pub",
                categoryId = 12,
                distance = 28
            ),
            Venue(
                name = "Indian Restaurant",
                categoryId = 33,
                distance = 28
            ),
            Venue(
                name = "Book Store",
                categoryId = 33,
                distance = 1
            ),
            Venue(
                name = "Church",
                categoryId = 12,
                distance = 28
            ),
            Venue(
                name = "Sports Arena",
                categoryId = 12,
                distance = 28
            )
        )
    }

}