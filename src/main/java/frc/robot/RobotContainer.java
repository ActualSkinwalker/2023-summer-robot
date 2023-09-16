// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.DriveCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.Auton.SimpleAuton;
import frc.robot.subsystems.Pneumatics;
import frc.robot.subsystems.Spinner;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.SwerveModule;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class RobotContainer {
  
  private final Swerve swerveSubsystem;
  private final Pneumatics pneumaticsSubsystem;
  private final AHRS gyro;
  private final Spinner spinnerSubsystem;
  private final DriveCommand driveCommand;
  private final IntakeCommand intakeCommand;
  private final SwerveModule flModule, frModule, rlModule, rrModule;
  private final Translation2d flModuleTranslation, frModuleTranslation, rlModuleTranslation, rrModuleTranslation;
  private XboxController driverController, commandController;

  public RobotContainer() {

    driverController = new XboxController(0);
    commandController = new XboxController(1);

    flModuleTranslation = new Translation2d(.43, .41);
    frModuleTranslation = new Translation2d(.43, -.41);
    rlModuleTranslation = new Translation2d(-.43, .41);
    rrModuleTranslation = new Translation2d(-.43, -.41);

    flModule = new SwerveModule(
      IDMaps.flSteerMotorID, 
      IDMaps.flDriveMotorID, 
      IDMaps.flEncoderID, flModuleTranslation);
    frModule = new SwerveModule(
      IDMaps.frSteerMotorID, 
      IDMaps.frDriveMotorID, 
      IDMaps.frEncoderID, frModuleTranslation);
    rlModule = new SwerveModule(
      IDMaps.rlSteerMotorID, 
      IDMaps.rlDriveMotorID, 
      IDMaps.rlEncoderID, rlModuleTranslation);
    rrModule = new SwerveModule(
      IDMaps.rrSteerMotorID, 
      IDMaps.rrDriveMotorID, 
      IDMaps.rrEncoderID, rrModuleTranslation);

    gyro = new AHRS();
    swerveSubsystem = new Swerve(flModule, frModule, rlModule, rrModule, gyro);
    pneumaticsSubsystem = new Pneumatics(new PneumaticsControlModule(13));
    spinnerSubsystem = new Spinner(IDMaps.leftSpinnerMotorID, IDMaps.rightSpinnerMotorID);

    driveCommand = new DriveCommand(
      swerveSubsystem, 
      () -> driverController.getLeftY(), 
      () -> driverController.getLeftX(), 
      () -> driverController.getRightX(), 
      () -> driverController.getAButton(),
      () -> driverController.getYButtonPressed(),
      () -> driverController.getXButton(),
      () -> driverController.getStartButton());
    
    intakeCommand = new IntakeCommand(
      pneumaticsSubsystem, 
      spinnerSubsystem, 
      () -> driverController.getLeftBumperPressed(),
      () -> driverController.getLeftBumperReleased(),
      () -> driverController.getBButton(),
      () -> driverController.getRightBumper());

    swerveSubsystem.setDefaultCommand(driveCommand);
    pneumaticsSubsystem.setDefaultCommand(intakeCommand);
  }

  /**Creates a new sendable field in the Analysis Tab of ShuffleBoard */
  public static void putSendable (String name, Sendable sendable) {
    Shuffleboard.getTab("Analysis Tab").add(name, sendable);
  }

  /**Creates a new sendable command in the Analysis Tab of ShuffleBoard */
  public static void putCommand (String name, CommandBase command, boolean canRunWhileDisabled) {
    putSendable(name, command.withName(name).ignoringDisable(canRunWhileDisabled));
  }

  public Command getAutonomousCommand() {
    return new SimpleAuton(swerveSubsystem);
  }
}
