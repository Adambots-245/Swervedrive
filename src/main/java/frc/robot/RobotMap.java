/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import frc.robot.Constants.DriveConstants;
import frc.robot.sensors.Gyro;

/**
 * Add your docs here.
 */
public class RobotMap {
    
    public static final Gyro GyroSensor = Gyro.getInstance();
    public static final Accelerometer Accelerometer = new BuiltInAccelerometer();
    // public static final WPI_TalonFX FrontRightMotor = new WPI_TalonFX(2);

}
