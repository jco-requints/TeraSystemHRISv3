package com.example.terasystemhrisv3.interfaces

interface NetworkRequestInterface
{
    fun beforeNetworkCall()
    fun afterNetworkCall(result: String?)
}