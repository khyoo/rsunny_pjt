jQuery.validator.addMethod('userid', function(value, element) {
	return this.optional(element) || /^[\w.]+$/i.test(value);
}, '(4자~12자 영문+숫자)');

jQuery.validator.addMethod('passwd', function(value, element) {
	return this.optional(element) ||  /^.*(?=.{10,20})(?=.*[0-9])(?=.*[a-zA-Z]).*$/i.test(value);
}, '(10자~20자 영문,숫자 조합)');

$.extend(jQuery.validator.messages, {
    required: '(필수)',
    remote: '(필수)',
    email: '(이메일 주소 확인)',
    url: '(필수)',
    date: '(필수)',
    dateISO: '(필수)',
    number: '(숫자)',
    digits: '(숫자)',
    equalTo: '값이 서로 다릅니다.',
    accept: '승낙해 주세요.',
    maxlength: $.validator.format('{0}글자 이상은 입력할 수 없습니다.'),
    minlength: $.validator.format('적어도 {0}글자는 입력해야 합니다.'),
    rangelength: $.validator.format('{0}글자 이상 {1}글자 이하로 입력해 주세요.'),
    range: $.validator.format('{0}에서 {1} 사이의 값을 입력하세요.'),
    max: $.validator.format('{0} 이하로 입력해 주세요.'),
    min: $.validator.format('{0} 이상으로 입력해 주세요.')
});
