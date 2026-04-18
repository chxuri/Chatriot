const mongoose = require("mongoose");
//imports mongoose

//defines shape of schema
const messageSchema = new mongoose.Schema({
    class_id: String,
    sender_name: String,
    content: String,
    createdAt: {
        type: Date,
        default: Date.now
    }
});

//creates model

const Message = mongoose.model("Message", messageSchema);