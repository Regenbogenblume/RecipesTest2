package com.breev.v2datamodel.data.dao

class InsertOrUpdate(val idOrCount:Long, val insertUpdate:Int){
    companion object {
        const val INSERT = 1
        const val UPDATE = 2
    }
}