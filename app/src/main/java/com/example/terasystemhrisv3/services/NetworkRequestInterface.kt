package com.example.terasystemhrisv3.services

interface NetworkRequestInterface
{
    fun beforeNetworkCall()
    fun afterNetworkCall(result: String?)
}