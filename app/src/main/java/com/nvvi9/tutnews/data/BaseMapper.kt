package com.nvvi9.tutnews.data

interface BaseMapper<in A, out B> {
    fun map(value: A): B?
}