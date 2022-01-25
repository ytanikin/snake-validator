package com.yeldos.snakevalidator.domain.fixture

import com.yeldos.snakevalidator.domain.Coordinate
import com.yeldos.snakevalidator.domain.Fruit

object FruitFixture {
    val fruit4to4 by lazy(LazyThreadSafetyMode.NONE) { Fruit( Coordinate(4, 4) ) }
}