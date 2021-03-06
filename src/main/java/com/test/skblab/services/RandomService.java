package com.test.skblab.services;

import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author Alexander Zubkov
 */
@Service
public class RandomService {

    boolean twoOfThree() {
        return new Random().nextInt(3) != 1;
    }

}
