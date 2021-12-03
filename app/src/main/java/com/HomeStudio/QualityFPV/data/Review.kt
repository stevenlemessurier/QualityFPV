package com.HomeStudio.QualityFPV.data

// Represents a review scraped from a product page
data class Review (
    var reviewTitle: String,
    var reviewScore: Double,
    var reviewDetails: String,
    var reviewDate: String
        )