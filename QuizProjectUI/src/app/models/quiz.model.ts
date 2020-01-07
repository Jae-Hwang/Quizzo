export class Quiz {

    constructor(
        public qid = 0,
        public name = '',
        public created = Date.now(),
        public createdBy = 0
    ) { }

}
