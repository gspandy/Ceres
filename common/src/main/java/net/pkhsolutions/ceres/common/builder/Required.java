/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.pkhsolutions.ceres.common.builder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author petter
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.CLASS)
public @interface Required {

}
