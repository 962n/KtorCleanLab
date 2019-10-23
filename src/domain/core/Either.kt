package com.lab.clean.ktor.domain.core

sealed class Either<out L, out R> {
    /** * Represents the left side of [Either] class which by convention is a "Failure". */
    data class Left<out L>(val a: L) : Either<L, Nothing>()
    /** * Represents the right side of [Either] class which by convention is a "Success". */
    data class Right<out R>(val b: R) : Either<Nothing, R>()

    val isRight get() = this is Right<R>
    val isLeft get() = this is Left<L>

    fun <L> left(a: L) = Left(a)
    fun <R> right(b: R) = Right(b)

    fun either(fnL: (L) -> Unit, fnR: (R) -> Unit): Any =
        when (this) {
            is Left -> fnL(a)
            is Right -> fnR(b)
        }
}

// Credits to Alex Hart -> https://proandroiddev.com/kotlins-nothing-type-946de7d464fb
// Composes 2 functions
fun <A, B, C> ((A) -> B).c(f: (B) -> C): (A) -> C = {
    f(this(it))
}

fun <T, L, R> Either<L, R>.rightFlatMap(fn: (R) -> Either<L, T>): Either<L, T> =
    when (this) {
        is Either.Left -> Either.Left(a)
        is Either.Right -> fn(b)
    }

fun <T, L, R> Either<L, R>.rightMap(fn: (R) -> (T)): Either<L, T> = this.rightFlatMap(fn.c(::right))

fun <T, L, R> Either<L, R>.leftFlatMap(fn: (L) -> Either<T, R>): Either<T, R> =
    when (this) {
        is Either.Left -> fn(a)
        is Either.Right -> Either.Right(b)
    }

fun <T, L, R> Either<L, R>.leftMap(fn: (L) -> (T)): Either<T, R> = this.leftFlatMap(fn.c(::left))