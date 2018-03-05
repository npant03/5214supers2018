package org.firstinspires.ftc.teamcode;

import android.provider.Settings;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="ManualUpdate", group="Team5214")
//@Disabled
public class ManualUpdate extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    //declares motors
    private DcMotor leftBack;
    private DcMotor rightBack;
    private DcMotor leftFront;
    private DcMotor rightFront;

    private DcMotor liftMotor;
    private DcMotor relicMotor;

    private DcMotor lBelt;
    private DcMotor rBelt;

    private Servo colSer;
    private Servo knckSer;


    private Servo rDum;
    private Servo lDum;
    private Servo cDum;
    private Servo wrist;
    private Servo finger;


    private int ticks;
    private int position2move2;


    long startTime = 0;


    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //hooks up all of these motors with the config file
        leftBack = hardwareMap.get(DcMotor.class, "LB");
        rightBack = hardwareMap.get(DcMotor.class, "RB");
        leftFront = hardwareMap.get(DcMotor.class, "LF");
        rightFront = hardwareMap.get(DcMotor.class, "RF");

        cDum = hardwareMap.servo.get("CD");
        rDum = hardwareMap.servo.get("RD");
        lDum = hardwareMap.servo.get("LD");
        colSer = hardwareMap.servo.get("COLORSERVO");
        knckSer = hardwareMap.servo.get("FLICKSERVO");
        wrist = hardwareMap.servo.get("WRIST");
        finger = hardwareMap.servo.get("FINGER");

        lBelt = hardwareMap.dcMotor.get("LBELT");
        rBelt = hardwareMap.dcMotor.get("RBELT");

        liftMotor = hardwareMap.dcMotor.get("LIFT");
        relicMotor = hardwareMap.dcMotor.get("RELICMOTOR");


        knckSer.setPosition(.5);
        colSer.setPosition(.2);

        waitForStart();


        while (opModeIsActive()) {

            colSer.setPosition(.2);
            // knckSer.setPosition(.5);
            //game pad one cotrls

            if (gamepad1.dpad_up) {
                lBelt.setPower(-1);
                rBelt.setPower(1);
            }
            if (gamepad1.dpad_left) {
                lBelt.setPower(0.5);
                rBelt.setPower(-0.5);
            }
            if (gamepad1.dpad_down) {
                lBelt.setPower(1);
                rBelt.setPower(-1);
            }
            if (gamepad1.dpad_right) {
                lBelt.setPower(0);
                rBelt.setPower(0);
            }
            bButton();
            xButton();
            if(gamepad1.right_bumper){lDum.setPosition(0.85); rDum.setPosition(.21); sleep(500); cDum.setPosition(0.7); lBelt.setPower(1); rBelt.setPower(-1);}
            if(gamepad1.y){lDum.setPosition(0.79); rDum.setPosition(0.27); sleep(500); cDum.setPosition(0.7);}
            if(gamepad1.left_bumper){cDum.setPosition(0.25); lDum.setPosition(0.3); rDum.setPosition(0.77);}
            if(gamepad1.a){lDum.setPosition(0.65); rDum.setPosition(0.4); cDum.setPosition(0.7); lBelt.setPower(0); rBelt.setPower(0);}
////            TARGET DOESNT WORK IF MOTOR IS STUCK AND PRESSED IN WRONG DIRECTION FIX IT
////            if (gamepad1.x){
////                motorWithEncoder(liftMotor,.5,8);
////            }
////            if (gamepad1.b){
////                motorWithEncoder(liftMotor,-.5,8);
////            }
//            if (gamepad1.x){
//                liftMotor.setPower(.5);
//                sleep(200);
//                liftMotor.setPower(0);
//            }
//            if (gamepad1.b){
//                liftMotor.setPower(-.5);
//                sleep(400);
//                liftMotor.setPower(0);
//            }

            //this is for game pad one olny right now -- hima -- 21:43 -- SAT feb 17

            /*if(gamepad2.a){
                elbow.setPosition(.75);
            }
            if (gamepad2.y){
                elbow.setPosition(0.25);
            }

            bButton();
            xButton();

            if (gamepad2.b){
                elbow.setPosition(0.50);
            }

            if (gamepad2.left_bumper) {
                hand.setPosition(0.25);
            }

            if (gamepad2.right_bumper) {
                hand.setPosition(0.75);
            }

            if (gamepad2.dpad_down) {
                worm.setPower(-.75);
            }

            if (gamepad2.dpad_right) {
                worm.setPower(0);

            }
            if (gamepad2.dpad_up) {
                worm.setPower(1);
            }
        */
            /*telemetry.update();
            leftBack.setPower(
                    .45*((gamepad1.left_stick_y + gamepad1.left_stick_x + (.5*gamepad1.right_stick_y)) + (.75 * -(gamepad1.right_stick_x))
                            + (.5 * (gamepad1.right_trigger)) + -.5 * (gamepad1.left_trigger))
                            + .9*((gamepad2.left_stick_y + gamepad2.left_stick_x + (.5*gamepad2.right_stick_y)) + (.75 * -(gamepad2.right_stick_x))
                            + (.5 * (gamepad2.right_trigger)) + -.5 * (gamepad2.left_trigger)));

            leftFront.setPower(
                    .45*((gamepad1.left_stick_y - gamepad1.left_stick_x+ (.5*gamepad1.right_stick_y) + (.75 * -(gamepad1.right_stick_x))
                            + (-.5 * (gamepad1.right_trigger)) + .5 * (gamepad1.left_trigger)))
                            +.9*((gamepad2.left_stick_y - gamepad2.left_stick_x+ (.5*gamepad2.right_stick_y) + (.75 * -(gamepad2.right_stick_x))
                            + (-.5 * (gamepad2.right_trigger)) + .5 * (gamepad2.left_trigger))));

            rightBack.setPower(
                    .45*((-gamepad1.left_stick_y + gamepad1.left_stick_x+ (-.5*gamepad1.right_stick_y) + (.75 * -(gamepad1.right_stick_x))
                            + (.5 * (gamepad1.right_trigger)) + -.5 * (gamepad1.left_trigger)))
                            +.9*((-gamepad2.left_stick_y + gamepad2.left_stick_x+ (-.5*gamepad2.right_stick_y) + (.75 * -(gamepad2.right_stick_x))
                            + (.5 * (gamepad2.right_trigger)) + -.5 * (gamepad2.left_trigger))));

            rightFront.setPower(
                    .45*((-gamepad1.left_stick_y - gamepad1.left_stick_x+ (-.5*gamepad1.right_stick_y) + (.75 * -(gamepad1.right_stick_x))
                            + (-.5 * (gamepad1.right_trigger)) + .5 * (gamepad1.left_trigger)))
                            +.9*((-gamepad2.left_stick_y - gamepad2.left_stick_x+ (-.5*gamepad2.right_stick_y) + (.75 * -(gamepad2.right_stick_x))
                            + (-.5 * (gamepad2.right_trigger)) + .5 * (gamepa
                            d2.left_trigger))));

            */


            //gamepad 2 contrls

            if(gamepad2.b){wrist.setPosition(.75);}
            if(gamepad2.x){wrist.setPosition(.25);}
            if(gamepad2.a){finger.setPosition(.75);}
            if(gamepad2.y){finger.setPosition(.25);}
            if     (gamepad2.dpad_right){relicMotor.setPower(-.3); }
            else if(gamepad2.dpad_left) {relicMotor.setPower(.3);}
            else if(gamepad2.dpad_down){motorWithEncoder(relicMotor,1,10);}
            else if(gamepad2.dpad_up){motorWithEncoder(relicMotor,1,-10);}
            else if(gamepad2.right_bumper) {}
            else if(gamepad2.left_bumper) {}
            else{relicMotor.setPower(0);}




            telemetry.update();
            leftBack.setPower(
                    .9*((gamepad1.left_stick_y + gamepad1.left_stick_x + (.5*gamepad1.right_stick_y)) + (.75 * -(gamepad1.right_stick_x))
                            + (.5 * (gamepad1.right_trigger)) + -.5 * (gamepad1.left_trigger))
                            + .45*((gamepad2.left_stick_y + gamepad2.left_stick_x + (.5*gamepad2.right_stick_y)) + (.75 * -(gamepad2.right_stick_x))
                            + (.75 * (gamepad2.right_trigger)) + -.75 * (gamepad2.left_trigger)));

            leftFront.setPower(
                    .9*((gamepad1.left_stick_y - gamepad1.left_stick_x+ (.5*gamepad1.right_stick_y) + (.75 * -(gamepad1.right_stick_x))
                            + (-.5 * (gamepad1.right_trigger)) + .5 * (gamepad1.left_trigger)))
                            +.45*((gamepad2.left_stick_y - gamepad2.left_stick_x+ (.5*gamepad2.right_stick_y) + (.75 * -(gamepad2.right_stick_x))
                            + (-.75 * (gamepad2.right_trigger)) + .75 * (gamepad2.left_trigger))));

            rightBack.setPower(
                    .9*((-gamepad1.left_stick_y + gamepad1.left_stick_x+ (-.5*gamepad1.right_stick_y) + (.75 * -(gamepad1.right_stick_x))
                            + (.5 * (gamepad1.right_trigger)) + -.5 * (gamepad1.left_trigger)))
                            +.45*((-gamepad2.left_stick_y + gamepad2.left_stick_x+ (-.5*gamepad2.right_stick_y) + (.75 * -(gamepad2.right_stick_x))
                            + (.75 * (gamepad2.right_trigger)) + -.75 * (gamepad2.left_trigger))));

            rightFront.setPower(
                    .9*((-gamepad1.left_stick_y - gamepad1.left_stick_x+ (-.5*gamepad1.right_stick_y) + (.75 * -(gamepad1.right_stick_x))
                            + (-.5 * (gamepad1.right_trigger)) + .5 * (gamepad1.left_trigger)))
                            +.45*((-gamepad2.left_stick_y - gamepad2.left_stick_x+ (-.5*gamepad2.right_stick_y) + (.75 * -(gamepad2.right_stick_x))
                            + (-.75 * (gamepad2.right_trigger)) + .75 * (gamepad2.left_trigger))));
        }






    }



    private void motorWithEncoder(DcMotor motorName, double power, int inches) {
        motorName.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        ticks = (int) (inches * 1120 / (4 * 3.14159)); //converts inches to ticks
        telemetry.addData("target position: ", ticks);
        telemetry.update();

        //modifies moveto position based on starting ticks position, keeps running tally
        motorName.setTargetPosition(ticks);
        motorName.setPower(power);

        while(motorName.isBusy()){
            leftBack.setPower(
                    .9*((gamepad1.left_stick_y + gamepad1.left_stick_x + (.5*gamepad1.right_stick_y)) + (.75 * -(gamepad1.right_stick_x))
                            + (.5 * (gamepad1.right_trigger)) + -.5 * (gamepad1.left_trigger))
                            + .45*((gamepad2.left_stick_y + gamepad2.left_stick_x + (.5*gamepad2.right_stick_y)) + (.75 * -(gamepad2.right_stick_x))
                            + (.75 * (gamepad2.right_trigger)) + -.75 * (gamepad2.left_trigger)));

            leftFront.setPower(
                    .9*((gamepad1.left_stick_y - gamepad1.left_stick_x+ (.5*gamepad1.right_stick_y) + (.75 * -(gamepad1.right_stick_x))
                            + (-.5 * (gamepad1.right_trigger)) + .5 * (gamepad1.left_trigger)))
                            +.45*((gamepad2.left_stick_y - gamepad2.left_stick_x+ (.5*gamepad2.right_stick_y) + (.75 * -(gamepad2.right_stick_x))
                            + (-.75 * (gamepad2.right_trigger)) + .75 * (gamepad2.left_trigger))));

            rightBack.setPower(
                    .9*((-gamepad1.left_stick_y + gamepad1.left_stick_x+ (-.5*gamepad1.right_stick_y) + (.75 * -(gamepad1.right_stick_x))
                            + (.5 * (gamepad1.right_trigger)) + -.5 * (gamepad1.left_trigger)))
                            +.45*((-gamepad2.left_stick_y + gamepad2.left_stick_x+ (-.5*gamepad2.right_stick_y) + (.75 * -(gamepad2.right_stick_x))
                            + (.75 * (gamepad2.right_trigger)) + -.75 * (gamepad2.left_trigger))));

            rightFront.setPower(
                    .9*((-gamepad1.left_stick_y - gamepad1.left_stick_x+ (-.5*gamepad1.right_stick_y) + (.75 * -(gamepad1.right_stick_x))
                            + (-.5 * (gamepad1.right_trigger)) + .5 * (gamepad1.left_trigger)))
                            +.45*((-gamepad2.left_stick_y - gamepad2.left_stick_x+ (-.5*gamepad2.right_stick_y) + (.75 * -(gamepad2.right_stick_x))
                            + (-.75 * (gamepad2.right_trigger)) + .75 * (gamepad2.left_trigger))));

        }

        motorName.setPower(0);
        motorName.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);



    }

    private void xButton(){
        if (gamepad1.x){
            startTime = System.currentTimeMillis();
            liftMotor.setPower(.5);
        }
        setSpe(750,700,0,startTime);

    }

    private void bButton(){
        if (gamepad1.b){
            startTime = System.currentTimeMillis();
            liftMotor.setPower(-.5);
        }
        setSpe(1800,1900,0,startTime);

    }

//    private void gp2X(){
//        if(gamepad2.x){
//            startTime = System.currentTimeMillis();
//            elbow.setPosition(.55);
//        }
//        setPos(1000,1100,.5, startTime, elbow);
//    }

    private void setSpe(long in, long out, double power, long sTime ){
        if((System.currentTimeMillis() >= (in+sTime)) && (System.currentTimeMillis() <= (out+sTime))){
            liftMotor.setPower(power);
        }
    }

    private void setPos(long in, long out, double pos, long sTime, Servo ser ){
        if((System.currentTimeMillis() >= (in+sTime)) && (System.currentTimeMillis() <= (out+sTime))){
            ser.setPosition(pos);
        }
    }


//    private void xButton(){
//        if (gamepad1.x){
//            startTime = System.currentTimeMillis();
//            liftMotor.setPower(.5);
//        }
//
//        setPos(100,300,.5,startTime);
//        setPos(300,500,.6,startTime);
//        setPos(500,700,.45,startTime);
//        setPos(900,1500,.8,startTime);
//
//
//
//    }
//
//    private void setPos(long in, long out, double alignInput, long sTime ) {
//        if ((System.currentTimeMillis() >= (in + sTime)) && (System.currentTimeMillis() <= (out + sTime))) {
//            align.setPosition(alignInput);
//        }
//    }
}