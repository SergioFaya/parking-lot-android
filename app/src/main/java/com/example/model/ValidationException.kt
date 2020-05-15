package com.example.model

import java.lang.RuntimeException

class ValidationException : RuntimeException {

    constructor(message: String): super(message)

}