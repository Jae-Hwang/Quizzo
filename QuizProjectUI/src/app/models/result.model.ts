export class Result {

    constructor(
        public userid = 0,
        public qid = 0,
        public grade = 0,
        public answers = '',
        public finished = Date.now()
    ) { }

}

/*
	private int userid;
	private int qid;
	private int grade;
	private String answers;
	private Timestamp finished;
*/
