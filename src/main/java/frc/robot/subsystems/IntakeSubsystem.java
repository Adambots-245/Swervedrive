/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.utils.Log;

public class IntakeSubsystem extends SubsystemBase {
  /*
   * Creates a new Intake.
   */

  private BaseMotorController intakeMotor;
  private DoubleSolenoid intakeSolenoid;
  private double motorSpeed = 0.0;

  public IntakeSubsystem(BaseMotorController intakeMotor, DoubleSolenoid intakeSolenoid) {
    super();
    
    this.intakeMotor = intakeMotor; 
    this.intakeMotor.setInverted(false);
    this.intakeSolenoid = intakeSolenoid;
    Log.info("Initializing Intake Subsystem");

    intakeMotor.setNeutralMode(NeutralMode.Brake);
  }

  public void intake(double speed) {
    Log.infoF("Intake - Speed: %f", speed);
    motorSpeed = speed;
  }

  public void stop(){
    Log.info("Stopping Intake Motor");
    motorSpeed = 0;
  }

  public void extendSolenoid () {
    intakeSolenoid.set(Value.kForward);
  }

  public void retractSolenoid () {
    intakeSolenoid.set(Value.kReverse);
  }

  @Override
  public void periodic() {
    intakeMotor.set(ControlMode.PercentOutput, motorSpeed);    
  }
}

