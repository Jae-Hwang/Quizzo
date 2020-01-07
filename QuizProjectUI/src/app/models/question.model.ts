export class Question {

    constructor(
        public quid = 0,
        public description = '',
        public type = 0,
        public selections = [''],
        public catagory = '',
        public created = Date.now(),
        public createdBy = 0
    ) { }

}

/*
private int quid;
	private String description;
	private int type;
	private String[] selections;
	private String catagory;
	private Timestamp created;
	private int createdBy;
*/
