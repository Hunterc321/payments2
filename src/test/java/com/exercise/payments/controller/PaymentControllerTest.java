package com.exercise.payments.controller;

import com.exercise.payments.model.PaymentDTO;
import com.exercise.payments.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentService paymentService;


    @Test
    @WithMockUser
    void testGetAllPayments() throws Exception {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setAmount(new BigDecimal("100.0"));
        paymentDTO.setCurrency("USD");

        Page<PaymentDTO> mockPage = new PageImpl<>(Collections.singletonList(paymentDTO), PageRequest.of(0, 10), 1);

        when(paymentService.getPaymentsWithFilters(any(), any(), any()))
                .thenReturn(mockPage);


        mockMvc.perform(get("/payments")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "amount")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].amount").value(paymentDTO.getAmount().toString()))
                .andExpect(jsonPath("$.content[0].currency").value(paymentDTO.getCurrency()));
    }

    @Test
    @WithMockUser
    void testCreatePayment() throws Exception {
        // Create a mock PaymentDTO
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setAmount(new BigDecimal("100.00"));
        paymentDTO.setCurrency("USD");
        paymentDTO.setFromAccount("from_account");
        paymentDTO.setToAccount("to_account");

        when(paymentService.createPayment(any())).thenReturn(paymentDTO);

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(paymentDTO)));
    }

    @Test
    @WithMockUser
    void testDeletePayment_Success() throws Exception {
        when(paymentService.deletePaymentById(1L)).thenReturn(true);

        mockMvc.perform(delete("/payments/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void testDeletePayment_NotFound() throws Exception {
        when(paymentService.deletePaymentById(1L)).thenReturn(false);

        mockMvc.perform(delete("/payments/1"))
                .andExpect(status().isNotFound());
    }
}