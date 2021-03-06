/*
 * Artificial Intelligence for Humans
 * Volume 3: Deep Learning and Neural Networks
 * Java Version
 * http://www.aifh.org
 * http://www.jeffheaton.com
 *
 * Code repository:
 * https://github.com/jeffheaton/aifh
 *
 * Copyright 2014-2015 by Jeff Heaton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * For more information on Heaton Research copyrights, licenses
 * and trademarks visit:
 * http://www.heatonresearch.com/copyright
 */
package com.heatonresearch.aifh.examples.ann;

import com.heatonresearch.aifh.ann.BasicLayer;
import com.heatonresearch.aifh.ann.BasicNetwork;
import com.heatonresearch.aifh.ann.Conv2DLayer;
import com.heatonresearch.aifh.ann.activation.ActivationReLU;
import com.heatonresearch.aifh.ann.activation.ActivationSoftMax;
import com.heatonresearch.aifh.ann.train.BackPropagation;
import com.heatonresearch.aifh.examples.learning.SimpleLearn;
import com.heatonresearch.aifh.util.MNISTReader;

public class LearnDigitsConv extends SimpleLearn {

    public void process() {
        System.out.println("Please wait, reading MNIST training data.");
        String dir = System.getProperty("user.dir");
        MNISTReader trainingReader = LearnDigitsBackprop.loadMNIST(dir,true);
        MNISTReader validationReader = LearnDigitsBackprop.loadMNIST(dir,false);

        System.out.println("Training set size: " + trainingReader.getNumImages());
        System.out.println("Validation set size: " + validationReader.getNumImages());

        int outputCount = trainingReader.getData().get(0).getIdeal().length;

        int[] inputShape = new int[] {trainingReader.getNumCols(),trainingReader.getNumCols(),3};

        BasicNetwork network = new BasicNetwork();
        network.addLayer(new BasicLayer(null,true,inputShape));
        network.addLayer(new Conv2DLayer(new ActivationReLU(),3,5,5));
        network.addLayer(new BasicLayer(new ActivationReLU(),true,25));
        network.addLayer(new BasicLayer(new ActivationSoftMax(),false,outputCount));
        network.finalizeStructure();
        network.reset();

        // train the neural network
        System.out.println("Training neural network.");
        final BackPropagation train = new BackPropagation(network, trainingReader.getData(), 1e-4, 0.9);
        train.setL1(0);
        train.setL2(1e-11);

        this.performIterationsClassifyEarlyStop(train, network, trainingReader.getData(), 5);
    }

    public static void main(String[] args) {

        LearnDigitsConv prg = new LearnDigitsConv();
        prg.process();
    }
}
