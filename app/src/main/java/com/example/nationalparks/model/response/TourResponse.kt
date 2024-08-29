package com.example.nationalparks.model.response

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "item", strict = false)
data class TourItemResponse(
    @field:Element(name="id") var id: String = "",
     @field:Element(name="title") var title: String = "",
     @field:Element(name="shortDescription") var shortDescription: String = "",
     @field:Element(name="thumb") var thumb: String = "",
     @field:Element(name="startDate") var startDate: String = "",
     @field:Element(name="endDate") var endDate: String = "",
     @field:Element(name="price") var price: Double = 0.0
)

@Root(name = "data", strict = false)
data class ToursResponse(
    @field:ElementList(inline = true, entry = "item")
    var tours: MutableList<TourItemResponse> = mutableListOf()
)