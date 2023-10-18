import React, {useEffect, useReducer} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import {Button, ButtonGroup, Container, Form, FormGroup, Input, Label, Table, Row, Col} from 'reactstrap';
import AppNavbar from '../AppNavbar';

export default function OrderEdit() {
    const { id } = useParams();
    const [order, setOrder] = useReducer((state, action) => {
        switch (action.type) {
            case 'set_order':
                return action.fetchedOrder
            case 'change_description':
                return {
                    ...state,
                    description: action.newDescription
                };
            case 'add_item': {
                let newItemList = state.items;
                newItemList.push(action.newItem);
                return {
                    ...state,
                    items: newItemList
                };
            }
            default: console.log('Unknown action: ' + action.type);
        }
    },{ description: '', items: [], users: [], closed: null });
    let currentUser = JSON.parse(localStorage.getItem('currentUser'));
    const navigate = useNavigate();

    useEffect(() => {
        fetch(`/orders/${id}`, {
            headers: { 'Authorization': 'Bearer ' + currentUser.token }
        })
            .then(response => response.json())
            .then(data => setOrder({ type: 'set_order', fetchedOrder: data }));
    }, [id, currentUser]);

    function handleOrderChange(event) {
        setOrder({ type: 'change_description', newDescription: event.target.value });
    }

    function handleItemChange(e, index) {
        let updatedOrder = order;
        updatedOrder.items[index][e.target.name] = e.target.value;
        setOrder(updatedOrder);
    }

    function handleSubmit(event) {
        event.preventDefault();

        fetch(('/orders/' + id), {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(order),
        })
            .then(response => response.json())
            .then(data => console.log(data));

        navigate('/orders');
    }

    const userList = order.users.map(i => {
        return (
            <tr key={i.id}>
                <td style={{whiteSpace: 'nowrap'}}>{i.username}</td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="danger">Eliminar</Button>
                    </ButtonGroup>
                </td>
            </tr>
        )
    });

    const itemList = order.items.map((item, index) => {
        return (
            <Row key={index}>
            <Col md={5}>
                <FormGroup>
                    <Input name="description" type="text" bsSize="sm" defaultValue={item.description}
                           onChange={(e) => handleItemChange(e, index)}/>
                </FormGroup>
            </Col>
            <Col md={2}>
                <FormGroup>
                    <Input name="quantity" type="number" bsSize="sm" defaultValue={item.quantity}
                           onChange={(e) => handleItemChange(e, index)}/>
                </FormGroup>
            </Col>
            <Col md={3}>
                <Button size="sm" color="danger">Eliminar</Button>
            </Col>
            </Row>
        )
    });

    function addItem() {
        let item = { description: 'Agregar ítem', quantity: 0 };

        let index = itemList.length;
        itemList.push(
            <Row key={index}>
            <Col md={5}>
                <FormGroup>
                    <Input name="description" type="text" bsSize="sm" defaultValue={item.description}
                           onChange={(e) => handleItemChange(e, index)}/>
                </FormGroup>
            </Col>
            <Col md={2}>
                <FormGroup>
                    <Input name="quantity" type="number" bsSize="sm" defaultValue={item.quantity}
                           onChange={(e) => handleItemChange(e, index)}/>
                </FormGroup>
            </Col>
            <Col md={3}>
                <Button size="sm" color="danger">Delete</Button>
            </Col>
            </Row>
        )
    }

    function removeItem() {
        let updatedItems = order.items;
        updatedItems.pop();
        setOrder(updatedItems);
        itemList.pop();
    }

    return (
        <div>
        <AppNavbar/>
        <Container>
            <h2 style={{paddingTop: 50, paddingBottom: 20}}>Editar pedido</h2>
            <Form onSubmit={handleSubmit}>
                <FormGroup>
                    <Label for="description">Descripción</Label>
                    <Input type="text" name="description" id="description" defaultValue={order.description || ''}
                           onChange={handleOrderChange} autoComplete="description"/>
                </FormGroup>
                <h3 style={{paddingTop: 40, paddingBottom: 20}}>Ítems</h3>
                {itemList}
                <FormGroup style={{paddingTop: 20}}>
                    <Button color="primary" onClick={() => addItem()}>Agregar ítem</Button>{' '}
                    <Button color="secondary" onClick={() => removeItem()}>Cancelar</Button>
                </FormGroup>
                <h3 style={{paddingTop: 40}}>Usuarios</h3>
                <Table className="mt-4">
                    <thead>
                        <tr>
                            <th width="60%">Nombre de usuario</th>
                            <th width="40%">Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        {userList}
                    </tbody>
                </Table>
                <FormGroup style={{paddingTop: 30}}>
                    <Button color="primary" type="submit">Guardar</Button>{' '}
                    <Button color="secondary" href="/orders">Cancelar</Button>
                </FormGroup>
            </Form>
        </Container>
    </div>
    );

}