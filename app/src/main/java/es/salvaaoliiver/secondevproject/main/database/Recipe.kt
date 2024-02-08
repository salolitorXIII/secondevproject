package es.salvaaoliiver.secondevproject.main.database

import java.io.Serializable

data class Recipe(
    var title: String,
    var steps: String,
    var imagePath: String,
    var public: Boolean
) : Serializable
