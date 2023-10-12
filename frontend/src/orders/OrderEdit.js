import React, {useEffect, useState} from 'react';
import {Link, useNavigate, useParams} from 'react-router-dom';
import {Button, ButtonGroup, Container, Form, FormGroup, Input, Label, Table} from 'reactstrap';
import AppNavbar from '../AppNavbar';

export default function OrderEdit() {
    const { id } = useParams();
    const [order, setOrder] = useState({ description: '', items: [], users: [] });
    const navigate = useNavigate();

    useEffect(() => {
        fetch(`/orders/${id}`)
            .then((response) => response.json())
            .then((data) => setOrder(data));
    }, [id]);

    function handleChange(event) {
        const name = event.target.name;
        const value = event.target.value;

        let updatedOrder = order;
        updatedOrder[name] = value;
        setOrder(updatedOrder);
    }

    function handleSubmit(event) {
        event.preventDefault();

        fetch(('/orders/' + id + '/1'), {
            method: 'PATCH',
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

    const itemList = order.items.map(i => {
        return (
            <tr key={i.id}>
                <td style={{whiteSpace: 'nowrap'}}>{i.description}</td>
                <td>{i.quantity}</td>
                <td>
                    <ButtonGroup>
                        <Link to={"/orders/" + order.id + "/items/" + i.id}>
                            <Button size="sm" color="primary">Add</Button>
                        </Link>
                        <Link to={"/orders/" + order.id + "/items/" + i.id}>
                            <Button size="sm" color="secondary">Remove</Button>
                        </Link>
                        <Button size="sm" color="danger">Delete</Button>
                    </ButtonGroup>
                </td>
            </tr>
        )
    });

    const userList = order.users.map(i => {
        return (
            <tr key={i.id}>
                <td style={{whiteSpace: 'nowrap'}}>{i.username}</td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="danger">Delete</Button>
                    </ButtonGroup>
                </td>
            </tr>
        )
    });

    return (
        <div>
        <AppNavbar/>
        <Container>
            <h2 style={{paddingTop: 50, paddingBottom: 50}}>Edit order</h2>
            <Form onSubmit={handleSubmit}>
                <FormGroup>
                    <Label for="description">Description</Label>
                    <Input type="text" name="description" id="description" defaultValue={order.description || ''}
                           onChange={handleChange} autoComplete="description"/>
                </FormGroup>
                <h3 style={{paddingTop: 50}}>Items</h3>
                <Table className="mt-4">
                    <thead>
                        <tr>
                            <th width="30%">Name</th>
                            <th width="30%">Quantity</th>
                            <th width="40%">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {itemList}
                    </tbody>
                </Table>
                <h3 style={{paddingTop: 50}}>Users</h3>
                <Table className="mt-4">
                    <thead>
                        <tr>
                            <th width="60%">Name</th>
                            <th width="40%">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {userList}
                    </tbody>
                </Table>
                <FormGroup style={{paddingTop: 50}}>
                    <Button color="primary" type="submit">Save</Button>{' '}
                    <Link to="/orders">
                        <Button color="secondary">Cancel</Button>
                    </Link>
                </FormGroup>
            </Form>
        </Container>
    </div>
    );

}