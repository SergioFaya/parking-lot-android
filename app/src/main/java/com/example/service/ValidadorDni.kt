package com.example.service

/**
 * Copiado del articulo https://medium.com/@manuelmato/c%C3%B3mo-validar-un-dni-en-java-parte-ii-f01170487d0b
 *
 * traducido a kotlin
 */
internal object ValidadorDNI {

    fun validar(dni: String): Boolean {
        var esValido = false
        var i = 0
        var caracterASCII = 0
        var letra = ' '
        var miDNI = 0
        var resto = 0
        val asignacionLetra = charArrayOf(
            'T',
            'R',
            'W',
            'A',
            'G',
            'M',
            'Y',
            'F',
            'P',
            'D',
            'X',
            'B',
            'N',
            'J',
            'Z',
            'S',
            'Q',
            'V',
            'H',
            'L',
            'C',
            'K',
            'E'
        )
        if (dni.length == 9 && Character.isLetter(dni[8])) {
            do {
                caracterASCII = dni.codePointAt(i)
                esValido = caracterASCII > 47 && caracterASCII < 58
                i++
            } while (i < dni.length - 1 && esValido)
        }
        if (esValido) {
            letra = Character.toUpperCase(dni[8])
            miDNI = dni.substring(0, 8).toInt()
            resto = miDNI % 23
            esValido = letra == asignacionLetra[resto]
        }
        return esValido
    }
}