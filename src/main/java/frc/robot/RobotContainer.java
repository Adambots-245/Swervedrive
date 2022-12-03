// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.Gamepad.Buttons;
import frc.robot.Gamepad.GamepadConstants;
import frc.robot.commands.ExtendIntakeCommand;
import frc.robot.commands.RetractIntakeCommand;
import frc.robot.commands.RunIntakeCommand;
import frc.robot.commands.StopIntakeCommand;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import java.util.List;

/*
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

    //Deadens input
    public static double deaden(double input, double sadDeadenVariable){
        if(Math.abs(input) < sadDeadenVariable){
            return 0;
        }else{
            return input;
        }
    }

// SmartDashboard.putNumber("Throttle: ", ex3dPro.getThrottle());

Joystick ex3dPro = new Joystick(OIConstants.kDriverControllerPort);
// The robot's subsystems
  private final DrivetrainSubsystem m_robotDrive = new DrivetrainSubsystem(ex3dPro);
  private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem(
      Constants.IntakeConstants.intakeMotor);

  // The driver's controller


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

    // Configure default commands
    if(true){
      m_robotDrive.setDefaultCommand(
            // The left stick controls translation of the robot.
            // Turning is controlled by the X axis of the right stick.
            new RunCommand(
            
              () ->
                    m_robotDrive.drive(
                    deaden(ex3dPro.getY(), 0.15),
                    deaden(ex3dPro.getX(), 0.15),
                    deaden(ex3dPro.getZ(), 0.3141592653589793238462643383279502884197169399),
                    false),
            m_robotDrive
            
            ));
    }else{
        m_robotDrive.setDefaultCommand(
            // The left stick controls translation of the robot.
            // Turning is controlled by the X axis of the right stick.
            new RunCommand(
            
              () ->
                    m_robotDrive.drive(
                    deaden(Buttons.primaryJoystick.getLeftY(), 0.15),
                    deaden(Buttons.primaryJoystick.getLeftX(), 0.3/*Math.PI*/),
                    deaden(Buttons.primaryJoystick.getRightX(), 0.15/*Math.PI*/),
                    false),
            m_robotDrive
            
            ));
    }

  }
  private void configureButtonBindings() {
    Buttons.primaryRB.whileHeld(new RunIntakeCommand(intakeSubsystem, 0.5));
    Buttons.primaryLB.whileHeld(new RunIntakeCommand(intakeSubsystem, -0.5));
    // Buttons.secondaryRB.whileHeld(new RunIntakeCommand(intakeSubsystem, 0.5));
    // Buttons.secondaryLB.whileHeld(new RunIntakeCommand(intakeSubsystem, -0.5));
    Buttons.primaryBButton.whenPressed(new ExtendIntakeCommand(intakeSubsystem));
    Buttons.primaryXButton.whenPressed(new RetractIntakeCommand(intakeSubsystem));
}

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // Create config for trajectory
    TrajectoryConfig config =
        new TrajectoryConfig(
                AutoConstants.kMaxSpeedMetersPerSecond,
                AutoConstants.kMaxAccelerationMetersPerSecondSquared)
            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(DriveConstants.kDriveKinematics);

    // An example trajectory to follow.  All units in meters.
    Trajectory exampleTrajectory =
        TrajectoryGenerator.generateTrajectory(
            // Start at the origin facing the +X direction
            new Pose2d(0, 0, new Rotation2d(0)),
            // Pass through these two interior waypoints, making an 's' curve path
            List.of(new Translation2d(1, 1), new Translation2d(2, -1)),
            // End 3 meters straight ahead of where we started, facing forward
            new Pose2d(3, 0, new Rotation2d(0)),
            config);

    var thetaController =
        new ProfiledPIDController(
            AutoConstants.kPThetaController, 0, 0, AutoConstants.kThetaControllerConstraints);
    thetaController.enableContinuousInput(-Math.PI, Math.PI);

    SwerveControllerCommand swerveControllerCommand =
        new SwerveControllerCommand(
            exampleTrajectory,
            m_robotDrive::getPose, // Functional interface to feed supplier
            DriveConstants.kDriveKinematics,

            // Position controllers
            new PIDController(AutoConstants.kPXController, 0, 0),
            new PIDController(AutoConstants.kPYController, 0, 0),
            thetaController,
            m_robotDrive::setModuleStates,
            m_robotDrive);

    // Reset odometry to the starting pose of the trajectory.
    m_robotDrive.resetOdometry(exampleTrajectory.getInitialPose());

    // Run path following command, then stop at the end.
    return swerveControllerCommand.andThen(() -> m_robotDrive.drive(0, 0, 0, false));
  }
}