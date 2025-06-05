package com.aagamshah.worldt2.utils

enum class Outcome(val displayText: String, val probability: Int) {
    ZERO("0 run", 5),
    ONE("1 run", 4),
    TWO("2 runs", 3),
    THREE("3 runs", 1),
    FOUR("4 runs", 2),
    SIX("6 runs", 1),
    WIDE("Wide", 1),
    NO_BALL("No Ball", 2),
    OUT("Out", 2)
}