package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import java.util.Locale;

/**
 * Created by hima on 2/1/18.
 */


@TeleOp(name="drivingAndTurningTest", group="Team5214")
@Disabled
public class drivingAndTurningTest extends LinearOpMode {

    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    // The IMU sensor object
    BNO055IMU imu;

    // State used for updating telemetry
    Orientation angles;
    Orientation angles2;
    Acceleration gravity;

    private DcMotor leftBack;
    private DcMotor rightBack;
    private DcMotor leftFront;
    private DcMotor rightFront;
    private int target;

    String headingNum = "";


    //----------------------------------------------------------------------------------------------
    // Main logic
    //----------------------------------------------------------------------------------------------

    @Override public void runOpMode() {

        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        leftBack  = hardwareMap.get(DcMotor.class, "LB");
        rightBack = hardwareMap.get(DcMotor.class, "RB");
        leftFront  = hardwareMap.get(DcMotor.class, "LF");
        rightFront = hardwareMap.get(DcMotor.class, "RF");

        leftBack.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.REVERSE);
        leftFront.setDirection(DcMotor.Direction.FORWARD);
        rightFront.setDirection(DcMotor.Direction.REVERSE);

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "GYRO");
        imu.initialize(parameters);

        // Set up our telemetry dashboard
        composeTelemetry();

        // Wait until we're told to go
        waitForStart();

        // Start the logging of measured acceleration
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
        angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        angles2   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        // Loop and update the dashboard
        while (opModeIsActive()) {
            straightWithEncoder(.25, 22);
            turnLeftDegress(10);
            straightWithEncoder(-.25, 33);
            turnLeftDegress(15);

            sleep(5000);
            telemetry.update();
        }
    }

    //----------------------------------------------------------------------------------------------
    // Telemetry Configuration
    //----------------------------------------------------------------------------------------------

    void composeTelemetry() {

        // At the beginning of each telemetry update, grab a bunch of data
        // from the IMU that we will then display in separate lines.
        telemetry.addAction(new Runnable() { @Override public void run()
        {
            // Acquiring the angles is relatively expensive; we don't want
            // to do that in each of the three items that need that info, as that's
            // three times the necessary expense.
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            gravity  = imu.getGravity();
        }
        });

        telemetry.addLine()
                .addData("status", new Func<String>() {
                    @Override public String value() {
                        return imu.getSystemStatus().toShortString();
                    }
                })
                .addData("calib", new Func<String>() {
                    @Override public String value() {
                        return imu.getCalibrationStatus().toString();
                    }
                });

        telemetry.addLine()
                .addData("heading", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.firstAngle);
                    }
                })
                .addData("roll", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.secondAngle);
                    }
                })
                .addData("pitch", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.thirdAngle);
                    }
                });

        telemetry.addLine()
                .addData("grvty", new Func<String>() {
                    @Override public String value() {
                        return gravity.toString();
                    }
                })
                .addData("mag", new Func<String>() {
                    @Override public String value() {
                        return String.format(Locale.getDefault(), "%.3f",
                                Math.sqrt(gravity.xAccel*gravity.xAccel
                                        + gravity.yAccel*gravity.yAccel
                                        + gravity.zAccel*gravity.zAccel));
                    }
                });
    }

    private void turn(double power, int time){
        //left turn is positive power
        leftBack.setPower(power); //sets left wheels to move backward
        leftFront.setPower(power);
        rightBack.setPower(-power); // makes right hand wheels to move forward
        rightFront.setPower(-power);
        sleep(time);
        //those things happen for this amount of time and then all the wheels stop
        leftBack.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        rightFront.setPower(0);
    }

    private void turnLeftDegress(double deg){
        Orientation agl = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double curent = Double.parseDouble(formatAngle(agl.angleUnit,agl.firstAngle));
        double start = curent;
        double stDeg = curent+deg;
        while (((curent) < (stDeg-1)) || (curent > (stDeg+1) )){
            telemetry.update();
            telemetry.addLine("IM IN THE WHILE");
            telemetry.addLine("start: " + Double.toString(start));
            telemetry.addLine("stDeg: " + Double.toString(stDeg));
            telemetry.addLine("deg: " + Double.toString(deg));
            telemetry.addLine("current: " + Double.toString(curent));
            turn(.5,100);
            agl   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            curent = Double.parseDouble(formatAngle(agl.angleUnit,agl.firstAngle));
            telemetry.update();
        }
        telemetry.addLine("I LEFT THE WHILE");
        telemetry.update();
    }

    //----------------------------------------------------------------------------------------------
    // Formatting
    //----------------------------------------------------------------------------------------------

    public String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    public String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }

    public String valueHead() {
        return formatAngle(angles.angleUnit, angles.firstAngle);
    }

    private void runMotorWithEncoder(DcMotor motorName, double power, int inches){ //creates function with parameters for motor name, power, and distance to move wheels


        int ticks = (int)(inches * 1120/ (4*3.14159)); //converts inches to ticks
        telemetry.addData("ticks: ", ticks); //print ticks
        telemetry.update();


        if(power < 0){
            target = motorName.getCurrentPosition() - ticks;
            while(motorName.getCurrentPosition() > target) { //while the encoder value hasn't reached the target
                motorName.setPower(power); //run motor at given power
                telemetry.addData("current tick value: ", motorName.getCurrentPosition());
                telemetry.update();

            }
        }

        else {
            target = motorName.getCurrentPosition() + ticks; //sets target to whatever the encoder reading is before anything happens plus number of ticks we want to add
            while(motorName.getCurrentPosition() < target) { //while the encoder value hasn't reached the target
                motorName.setPower(power); //run motor at given power
                telemetry.addData("current tick value: ", motorName.getCurrentPosition());
                telemetry.update();

            }
        }

        telemetry.addData("target: ", target); //print target
        telemetry.update();



        motorName.setPower(0); //after encoder value reaches target turn the motor off.
    }

    private void straightWithEncoder(double power, int straightInches){
        runMotorWithEncoder(leftBack, power, straightInches);
        runMotorWithEncoder(leftFront, power, straightInches);
        runMotorWithEncoder(rightBack, power, straightInches);
        runMotorWithEncoder(rightFront, power, straightInches);
        telemetry.update();
    }

    private void strafeWithEncoder(double power, int strafeInches){
        runMotorWithEncoder(leftBack, power, strafeInches);
        runMotorWithEncoder(leftFront, -power, strafeInches);
        runMotorWithEncoder(rightBack, power, strafeInches);
        runMotorWithEncoder(rightFront, -power, strafeInches);
        telemetry.update();
    }
}