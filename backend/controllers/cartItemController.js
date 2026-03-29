import CartItem from '../models/cartItem.js'

const Add = async (req, res) => {
    try {
        const cartItem = new CartItem(req.body);
        await cartItem.save(); 
        res.json(cartItem); 
    } catch (ex) {
        res.status(500).json({ error: ex.message });
    }
};


export {Add}