const instance = axios.create({
    baseURL: '/book',
    timeout: 1000
});

function getIndex(list, id) {
    for (var i = 0; i < list.length; i++ ) {
        if (list[i].id === id) {
            return i;
        }
    }
    return -1;
}

Vue.component('book-table', {
    props: ['books'],
    data: function () {
        return {book : null};
    },
    template:
        '<div>' +
            '<book-form :books = "books" :bookAttr = "book"/>' +
            '<div/>' +
            '<table>' +
                '<thead>' +
                    '<tr>' +
                        '<th>Название</th>' +
                        '<th>Автор</th>' +
                        '<th>Жанр</th>' +
                        '<th>Действие1</th>' +
                        '<th>Действие2</th>' +
                    '</tr>' +
                '</thead>' +
                '<tbody><book-rows v-for="book in books" :book = "book" :books = "books" ' +
                ':key = "book.id" :editMethod = "editMethod"/>' +
                '</tbody>' +
            '</table>' +
        '</div>',

    methods: {
        editMethod: function (book) {
            this.book = book;
        }
    }
});

Vue.component('book-rows', {
    props: ['book', 'editMethod', 'books'],
    template:
        '<tr>' +
            '<td>{{book.title}}</td>' +
            '<td v-if="book.author">{{book.author.firstName}}&nbsp;{{book.author.secondName}}&nbsp;{{book.author.birthday}}</td>' +
            '<td v-if="book.genre">{{book.genre.name}}</td><td v-else>Проблема здесь</td>' +
            '<td><input type="image" src="edit.png" @click = "edit"></td>' +
            '<td><input type="image" src="del.png" @click ="del"/></td>' +
        '</tr>',
    methods: {
        del: function() {
            instance.delete('/' + this.book.id).then(res => {
                if (res.status === 200) {
                    this.books.splice(this.books.indexOf(this.book), 1)
                }
            })
        },
        edit: function () {
            this.editMethod(this.book);
        }
    }
});



Vue.component('book-form', {
    props: ['bookAttr', 'books'],
    data: function () {
        return {
            id: '',
            title: '',
            genre: '',
            firstName: '',
            secondName: '',
            birthday: ''
        }
    },
    watch: {
        bookAttr: function(newVal, oldVal){
            this.id = newVal.id;
            this.title = newVal.title;
            this.genre = newVal.genre.name;
            this.firstName = newVal.author.firstName;
            this.secondName = newVal.author.secondName;
            this.birthday = newVal.author.birthday;
        }
    },
    template:
            '<div>' +
                '<h3 v-if="id">Изменение книги:</h3>' +
                '<h3 v-else>Добавление книги:</h3>' +
                '<div><input type="text" placeholder="Название" v-model="title"/></div>' +
                '<div><input type="text" placeholder="Жанр" v-model="genre"/></div>' +
                '<div><h3>Автор</h3></div>' +
                '<div><input type="text" placeholder="Имя" v-model="firstName"/></div>' +
                '<div><input type="text" placeholder="Фамилия" v-model="secondName"/></div>' +
                '<div><input type="date" placeholder="День рождения" v-model="birthday"/></div>' +
                '<div><input type="button" value="Сохранить" @click="save"/></div>' +
            '</div>',

    methods: {
        save: function () {
            var author = {
                firstName: this.firstName,
                secondName: this.secondName,
                birthday: this.birthday
            };
            var genre = {
                name: this.genre
            };
            var book = {
                title: this.title,
                genre: genre,
                author: author
            };
            if (this.title ===''){
                alert('Заполните название')
            }
            else if (this.genre ===''){
                alert('Заполните жанр')
            }
            else if (this.firstName ===''){
                alert('Заполните имя автора')
            }
            else if (this.secondName ===''){
                alert('Заполните фамилию автора')
            }
            else if (this.birthday ===''){
                alert('Заполните день рождения автора')
            }
            else {
                if (this.id){
                    instance.put('/' + this.bookAttr.id, book).then(res => {
                        instance.put('/' + this.bookAttr.id, book).then(data => {
                            var bookData = data.data;
                            var index = getIndex(this.books, bookData.id);
                            this.books.splice(index, 1, bookData);
                        })
                    })
                }

                else {
                    instance.post('',book).then(data => {this.books.push(book)});
                }
                this.id = '';
                this.title = '';
                this.genre = '';
                this.firstName = '';
                this.secondName = '';
                this.birthday = '';
            }
        }
    }
});

new Vue({
    el: '#app',
    data: {
        books: [],
        book: null
    },
    template: '<div>' +
                '<book-table :books = "books"/>' +
            '</div>',

    created() {
        instance
            .get('')
            .then((response) => {response.data.forEach(book=> this.books.push(book))})
            .catch(e => {console.log(e)})
        }
});