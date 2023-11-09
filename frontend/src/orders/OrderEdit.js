import {useParams} from "react-router";
import React, {useEffect, useState} from "react";
import {Button, Container, Form, FormGroup, Input, Label} from "reactstrap";
import AppNavbar from "../navbar/AppNavbar";
import {ItemList} from "../items/ItemList";
import {SuccessMessage} from "./SuccessMessage";
import {UserList} from "../share-users/UserList";
import {useNavigate} from "react-router-dom";

export default function OrderEdit() {
    const { id } = useParams();
    const [order, setOrder] = useState({
        id: '',
        description: '',
        closed: false
    });
    const [items, setItems] = useState([]);
    const [users, setUsers] = useState([]);
    const [success, setSuccess] = useState(false);
    let token = localStorage.getItem('token');
    const navigate = useNavigate();

    useEffect(() => {
        function fetchConst(path, setter) {
            fetch("/orders/" + id + path, {
                method: 'GET',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token
                }
            })
                .then(response => response.json())
                .then(data => {
                    setter(data)
                });
        }

        fetchConst("", setOrder);
        fetchConst("/items", setItems);
        fetchConst("/users", setUsers);
    }, [id, token]);

    function handleSubmit(e) {
        e.preventDefault();

        fetch("/orders/" + id, {
            method: 'PATCH',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            body: JSON.stringify({
                description: order.description
            })
        })
            .then(response => {
                if (!response.ok) throw new Error(response.statusText);
            })
            .then(() => setSuccess(true))
            .catch(console.log);
    }

    function createItem() {
        fetch("/orders/" + id + "/items", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        })
            .then(response => response.json())
            .then(data => {
                navigate("/orders/" + id + "/items/" + data.id);
            });
    }

    return <div>
        <AppNavbar/>
        <Container>
            <h2 style={{ paddingTop: 50, paddingBottom: 50 }}>Editar pedido</h2>
            <Form onSubmit={handleSubmit} style={{paddingBottom:20}}>
                <FormGroup>
                    <Label for="description">Descripción</Label>
                    <Input type="text" id="description" defaultValue={order.description}
                           onChange={e => {
                               setSuccess(false);
                               setOrder({...order, description: e.target.value});
                           }}/>
                </FormGroup>
                <SuccessMessage success={success}/>
                <FormGroup>
                    <Button color="primary" type="submit">Guardar</Button>
                </FormGroup>
            </Form>
            {
                items.length ?
                    <ItemList items={items} setItems={setItems} orderId={id}/> :
                    <div> No hay ítems </div>
            }
            <div style={{ paddingTop: 30, paddingBottom: 50 }}>
                <Button color="primary" onClick={() => createItem()}>Agregar ítem</Button>
            </div>
            {
                users.length ?
                    <UserList users={users} setUsers={setUsers} orderId={id}/> :
                    <div> No se compartió con ningún usuario </div>
            }
            <div style={{  paddingTop: 30, paddingBottom: 50 }}>
                <Button color="primary" href={"/orders/" + id + "/users"}>Compartir</Button>
            </div>
            <div>
                <Button color="success" href={"/orders"}>Volver</Button>
            </div>
        </Container>
    </div>
}