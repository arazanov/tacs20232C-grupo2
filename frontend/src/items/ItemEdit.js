import {useEffect, useState} from "react";
import {useParams} from "react-router";
import AppNavbar from "../navbar/AppNavbar";
import {Button, Col, Container, Form, FormGroup, Input, Label} from "reactstrap";
import {useNavigate} from "react-router-dom";

export default function ItemEdit() {
    const { id } = useParams();
    const [item, setItem] = useState({
        description: '',
        quantity: null
    });
    const navigate = useNavigate();

    useEffect(() => {
        let token = localStorage.getItem('token');
        fetch('/items/' + id, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        })
            .then(response => response.json())
            .then(data => {
                setItem(data);
            });
    }, [id]);

    function handleSubmit(e) {
        e.preventDefault();
        let token = localStorage.getItem('token');
        fetch('/items/' + id, {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            body: JSON.stringify(item)
        }).then(response => {
            if (response.ok) navigate(-1);
        })
    }

    return <div>
        <AppNavbar/>
        <Container>
            <h2 style={{paddingTop: 50, paddingBottom: 50}}>Editar ítem</h2>
            <Form onSubmit={handleSubmit}>
                <FormGroup row>
                    <Label for="description" sm={2}>Descripción</Label>
                    <Col sm={10}>
                        <Input type="text" id="description" defaultValue={item.description} autoComplete={"off"}
                               onChange={e => setItem({
                                   ...item, description: e.target.value
                               })}/>
                    </Col>
                </FormGroup>
                <FormGroup row>
                    <Label for="quantity" sm={2}>Cantidad</Label>
                    <Col sm={10}>
                        <Input type="number" id="quantity" defaultValue={item.quantity}
                               onChange={e => setItem({
                                   ...item, quantity: e.target.value
                               })}/>
                    </Col>
                </FormGroup>
                <FormGroup style={{paddingTop: 50}}>
                    <Button color="primary" type="submit">Guardar</Button>{' '}
                    <Button color="secondary" onClick={() => navigate(-1)}>Cancelar</Button>
                </FormGroup>
            </Form>
        </Container>
    </div>

}