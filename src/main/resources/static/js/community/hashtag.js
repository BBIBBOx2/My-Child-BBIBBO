let inputElm = document.querySelector('input[name=tag]');
var tagify = new Tagify(inputElm);

tagify.on('add', function() {
    console.log(tagify.value);
})