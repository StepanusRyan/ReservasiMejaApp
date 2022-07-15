package com.stepanusryan.reservasimeja.model

data class Pesanan (
    var idPesanan:String ?= "",
    var nomorMeja:String ?= "",
    var harga:String ?= "",
    var tanggal:String ?= "",
    var nama:String ?= "",
    var telepon:String ?= "",
    var jumlahOrang:String ?= "",
    var keperluan:String ?= ""
    )