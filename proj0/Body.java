public class Body {
	public double xxPos, yyPos, xxVel, yyVel, mass;
	public String imgFileName;
	public double constG = 6.67e-11;

	public Body(double xP, double yP, double xV, double yV, double m, String img){
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}

	public Body(Body b){
		this.xxPos = b.xxPos;
		this.yyPos = b.yyPos;
		this.xxVel = b.xxVel;
		this.yyVel = b.yyVel;
		this.mass = b.mass;
	}

	public double calcDistance(Body b2){
		double xxdist = b2.xxPos - this.xxPos;
		double yydist = b2.yyPos - this.yyPos;
		double rrdistSquare = xxdist * xxdist + yydist * yydist;
		double rrdist = Math.sqrt(rrdistSquare);
		return rrdist;
	}

	public double calcForceExertedBy(Body b3) {
		double force = constG * this.mass * b3.mass / (this.calcDistance(b3) * this.calcDistance(b3));
		return force;
	}

	public double calcForceExertedByX(Body b) {
		double xxdist = b.xxPos - this.xxPos;
		return this.calcForceExertedBy(b) * xxdist / this.calcDistance(b);
	}

	public double calcForceExertedByY(Body b) {
		double yydist = b.yyPos - this.yyPos;
		return this.calcForceExertedBy(b) * yydist / this.calcDistance(b);
	}

	public double calcNetForceExertedByX(Body[] b) {
		double xxdistSum = 0;
		double forceSum = 0;
		for(Body s : b) {
			if(this.equals(s)){
				continue;
			} else {
				double xxdist = s.xxPos - this.xxPos;
				xxdistSum += xxdist;
				forceSum += this.calcForceExertedBy(s) * xxdist / this.calcDistance(s);
			}
			
		}
		return forceSum;
	}

	public double calcNetForceExertedByY(Body[] b) {
		double yydistSum = 0;
		double forceSum = 0;
		for(Body s : b) {
			if(this.equals(s)){
				continue;
			} else {
				double yydist = s.yyPos - this.yyPos;
				yydistSum += yydist;
				forceSum += this.calcForceExertedBy(s) * yydist / this.calcDistance(s);
			}
			
		}
		return forceSum;
	}

	public void update(double dt, double fX, double fY){
		double a_x = fX / this.mass;
		double a_y = fY / this.mass;
		xxVel += dt * a_x;
		yyVel += dt * a_y;
		xxPos += dt * xxVel;
		yyPos += dt * yyVel;
	}

	/*为了能使用non-static variables，没有用static*/
	public void draw() {
		//StdDraw.enableDoubleBuffering();
		//StdDraw.clear();
		//StdDraw.setScale(-radius, radius);
		StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
		//StdDraw.show();
		//StdDraw.pause(100);
		//StdDraw.clear();
	}
}