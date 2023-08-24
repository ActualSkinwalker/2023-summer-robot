// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Pneumatics extends SubsystemBase {
  
  DoubleSolenoid solenoid;

  PneumaticsControlModule PCM;

  /**Creates a new Pneumatics subsystem with a PCM and two solenoids */
  public Pneumatics(PneumaticsControlModule PCM) {
    this.PCM = PCM;

    solenoid = PCM.makeDoubleSolenoid(0, 0); //TODO: Find channel values
  }

  /**Runs the toggle() method on both solenoids.*/
  public void toggleSolenoid () {
    solenoid.toggle();
  }

  /**Enables the subsystem. Returns true if the compressor was enabled, false if not. 
   * Note: compressor would only not be enabled if it already was.*/
  public boolean enableSubsystem() {
    if (solenoid.get() == DoubleSolenoid.Value.kOff) solenoid.set(Value.kReverse);
    if (!PCM.getCompressor()) {
    PCM.enableCompressorDigital();
    return true;
    }
    else return false;
  }

  /**Sets both solenoids to "off", then disables the compressor. */
  public void disableSubsystem () {
    solenoid.set(Value.kReverse); //TODO: Find which channel extends/retracts to determine which direction is undeployed
    solenoid.set(Value.kOff);
    PCM.disableCompressor();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
