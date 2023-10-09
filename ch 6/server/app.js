const express = require("express");
const app = express();
const PORT = 3000;
const datas = require("./db/data.json");
const fs = require('fs');

app.use(express.json());
app.use(express.urlencoded({ extended : true}))

app.get('/api/posts', (req, res) => {
    res.status(200).json(datas);
})

app.get('/api/posts/:id', (req, res) => {
    let idNow = req.params.id;
    let current;
    for (let i = 0; i < datas.length; i++) {
        if (idNow == datas[i].id) {
            current = datas[i];
        }
    }
    res.status(200).json(current);
})

app.post('/api/posts',(req,res)=>{
    let lastId = datas[datas.length - 1].id + 1;
    const  {title,body} = req.body;
    console.log(lastId);
    let temp = {
        id: lastId,
        title,
        body
    }

    datas.push(temp);
    let stringData = JSON.stringify(datas);
    const newData = fs.writeFileSync('./db/data.json', stringData);
    res.status(201).json(newData);
    
})

app.put('/api/posts/:id', (req,res)=>{
    const { title, body } = req.body;
    let idNow = parseInt(req.params.id);
    let current;
    for (let i = 0; i < datas.length; i++) {
        if (idNow === datas[i].id) {
            current = {
                id : idNow,
                title,
                body
            };
            datas[i] = current;
        }
    }
    let updatedData = JSON.stringify(datas);
    const newerData = fs.writeFileSync('./db/data.json', updatedData);
    res.status(201).json(datas);
    
})




app.listen(PORT, () => {
    console.log(`Tes Tes Tes ${PORT} `);
})